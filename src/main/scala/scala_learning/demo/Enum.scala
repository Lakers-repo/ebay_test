package scala_learning.demo

object Enum extends Enumeration {
  //  val North = Value("North")
  //  val East = Value("East")
  //  val South = Value("South")
  //  val West = Value("West")
  //

  //这行是可选的，类型别名，在使用import语句的时候比较方便，建议加上
  type Demo09 = Value

  val Kafka, Avro, Custom = Value

  def loadData(sourceType: Demo09) {
    sourceType match {
      case Kafka => println("sourceType is " + sourceType)
      case Avro => println("sourceType is " + sourceType)
      case Custom => println("sourceType is " + sourceType)
      case _ => println("Unknown type")
    }
  }
}
