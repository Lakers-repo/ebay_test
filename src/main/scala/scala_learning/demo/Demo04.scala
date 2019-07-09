package scala_learning.demo

/**
  * Scala的高阶方法
  */
object Demo04 {
  def main(args: Array[String]): Unit = {
    //    println(List.range(1, 5)) 1,2,3,4
    //    println(List.range(1,1)) List()
    val tuples = List.range(1, 5) flatMap {
      i =>
        List.range(1, i) map {
          j => (i, j)
        }
    }
    println(tuples)
  }
}
