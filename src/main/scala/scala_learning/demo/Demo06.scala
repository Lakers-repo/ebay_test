package scala_learning.demo

import scala.collection.immutable.Queue

/**
  * 这里不能用new Queue，因为构造器是protected修饰的，只有是它的子类才可以访问构造器
  */
object Demo06 {
  def main(args: Array[String]): Unit = {
    val empty = Queue()
    val has1 = empty.enqueue(1)
    val has123 = has1.enqueue(List(2,3))
    //    println(has123)
    val dequeue: (Int, Queue[Int]) = has123.dequeue
    println(dequeue)
  }
}
