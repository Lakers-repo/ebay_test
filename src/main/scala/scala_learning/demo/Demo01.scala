package scala_learning.demo

import scala_learning.case_class.Rational

object Demo01 {
  def main(args: Array[String]): Unit = {
//    val str = "String"
//    println(str toLowerCase())
    val rational1 = new Rational(1,2)
    val rational2 = new Rational(3,4)

    println(rational1.demon)
    println(rational1.nmber)

    println(rational2.demon)
    println(rational2.nmber)

    println(rational1 add rational2)
  }
}
