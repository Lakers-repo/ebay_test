package flink

import java.text.SimpleDateFormat

import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.watermark.Watermark

object StreamingPeriodicWatermark extends AssignerWithPeriodicWatermarks[(String,Long)] {

  var currentMaxTimestamp = 0L
  val maxOutOfOrderness = 10000L // 最大允许的乱序时间是10s

  val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

  /**
    * 定义生成watermark的逻辑
    *
    * @return
    */
  override def getCurrentWatermark: Watermark = {
    new Watermark(currentMaxTimestamp - maxOutOfOrderness)
  }

  /**
    * 定义如何提取timestamp
    *
    * @param element
    * @param previousElementTimestamp
    * @return
    */
  override def extractTimestamp(element: (String, Long), previousElementTimestamp: Long): Long = {
    val timestamp = element._2
    currentMaxTimestamp = Math.max(currentMaxTimestamp, timestamp)
    println("key:" + element._1 + ",eventtime:[" + element._1 + "|" + sdf.format(element._2) + "], currentMaxTimestamp:[" + currentMaxTimestamp + "|" +
      sdf.format(currentMaxTimestamp) + "], watermark:[" + getCurrentWatermark().getTimestamp() + "|" + sdf.format(getCurrentWatermark().getTimestamp()) + "]")

    timestamp
  }
}
