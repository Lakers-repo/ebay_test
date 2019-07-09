package scala_learning.demo

/**
  * 折叠操作 左折叠 右折叠 都需要三个参数  开始值 列表 二元操作符
  */
object Demo05 {
  def main(args: Array[String]): Unit = {
    val list = List(1,3,3)
    val i = sum(list)
    val j = product(list)
  }

  def sum(xs:List[Int]):Int = {
    (0 /: xs) (_ + _)
  }

  def product(xs:List[Int]):Int = {
    (1 /: xs) (_ * _)
  }

  def concat(words:List[String]):String = {
    (words.head /: words) (_ + "" + _)
  }
}
