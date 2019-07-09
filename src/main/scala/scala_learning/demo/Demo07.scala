package scala_learning.demo

import scala.collection.mutable

/**
  * scala 的堆栈 先进后出 push pop
  */
object Demo07 {
  def main(args: Array[String]): Unit = {
    val stack = new mutable.Stack[Int]
    stack.push(1)
    stack.push(2)
    stack.pop()
    println(stack.top)
  }
}
