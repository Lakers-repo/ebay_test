package scala_learning.demo

/**
  * Scala的简单插入排序
  */
object Demo02 {
  def main(args: Array[String]): Unit = {
    val xs = List(5, 3, 4)
    //    isSort(xs)
    println(isSort(xs).apply(2))
  }


  //  def insert(x: Int, xs: List[Int]): List[Int] =
  //    if(xs.isEmpty || x < xs.head) x::xs
  //    else xs.head::insert(x,xs.tail)
  //
  //  def isSort(xs:List[Int]):List[Int] =
  //    if(xs.isEmpty) Nil
  //    else insert(xs.head,isSort(xs.tail))

  //模式匹配
  def isSort(xs: List[Int]): List[Int] = xs match {
    case List() => List()
    case x :: xs1 => insert(x, isSort(xs1))
  }

  def insert(x: Int, xs: List[Int]): List[Int] = xs match {
    case List() => List(x)
    case y :: ys => if (x < y) x :: xs else y :: insert(x, ys)
  }
}
