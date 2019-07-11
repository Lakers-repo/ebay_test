package flink

import java.text.SimpleDateFormat

import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.{CheckpointingMode, TimeCharacteristic}
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.scala._
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.scala.{OutputTag, StreamExecutionEnvironment}

object StreamingWindowWatermark {
  def main(args: Array[String]): Unit = {
    var port:Int = 9999
    var hostname:String = "localhost"

//    try{
//      val parameterTool = ParameterTool.fromArgs(args)
//      hostname = parameterTool.get("hostname")
//      port = parameterTool.getInt("port")
//    }catch {
//      case e:Exception => {
//        System.err.println("USAGE: \n StreamingWindowWatermark <hostname> <host>")
//        System.exit(1)
//      }
//    }

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    env.enableCheckpointing(1000)

    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)

    env.setParallelism(1)

    val input = env.socketTextStream(hostname,port)

    val dataDstream = input.map(record => {
      val arr = record.split(",")
      (arr(0),arr(1).toLong)
    })

    val waterMarkStream = dataDstream.assignTimestampsAndWatermarks(StreamingPeriodicWatermark)

    // 保存被丢弃的乱序数据
    val outputTag = new OutputTag[(String, Long)]("late-data")
    val window = waterMarkStream
      .keyBy(0)
      .window(TumblingEventTimeWindows.of(Time.seconds(3))) // 按照消息的EventTime分配窗口，和调用TimeWindow效果一样
      .allowedLateness(Time.seconds(2)) // 允许延迟2s
      .sideOutputLateData(outputTag)
      .apply(new WindowFunction[(String, Long), String, Tuple, TimeWindow]() {
        override def apply(tuple: Tuple, window: TimeWindow, input: Iterable[(String, Long)], out: Collector[String]): Unit = {
          val key = tuple.toString
          val arrarList = input.toList.sortBy(_._2)
          val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
          val result = key + "," + arrarList.size + "," + sdf.format(arrarList(0)) + "," +
            "" + sdf.format(arrarList(arrarList.size - 1)) + "," + sdf.format(window.getStart()) + "," + sdf.format(window.getEnd())

          out.collect(result)
        }
      })

    // 把延迟的数据暂时打印到控制台，实际可以保存到存储介质中。
    val sideOutput = window.getSideOutput(outputTag)
    sideOutput.print()

    window.print()

    // 因为flink是懒加载的，所以必须调用execute方法才会执行上面的代码
    env.execute("eventTime-watermark")

  }
}
