package scala_learning.demo

/**
  * scala的枚举 路径依赖类型 Demo08就是路径 Value就是依赖类型
  */
object Demo08 {
  def main(args: Array[String]): Unit = {
//    for(d <- Demo09.values){
//      println(d + " ")
//    }

//    for (s <- Demo09.values) println(s.id + ":" + s)

    import Enum.Demo09

    Enum.loadData(Enum.Kafka)
  }

//  val Red = Value
//  val Green = Value
//  val Blue = Value

//  val Red,Green,Blue = Value
}
