package scala_learning.case_class

class Rational(a: Int, b: Int) {

  // 辅助构造器 每个构造器的调用终究是以调用主构造器为结束
  // 只有主构造器才能调用超类的构造器
  def this(a:Int) = this(a,1)

  private val g = gcd(a.abs,b.abs)

  val nmber: Int = a / g
  val demon: Int = b / g

  // println("created "+a+"/"+b)


  // 先决条件：是传递给方法和构造器的限制，是调用者必须满足的要求

  require(b != 0)

  // 重写父类的toString方法，否则打印对象的引用时，默认是调用父类的toString方法，满足不了我们的需求

  override def toString: String = {
    a + "/" + b
  }

  def add(that: Rational): Rational = {
    return new Rational(
      nmber * that.demon + that.nmber * demon,
      demon * that.demon
    )
  }

  def lessThan(that: Rational) = {
    //这里的this可以去掉
    this.nmber * that.nmber < that.nmber * this.demon
  }

  def max(that: Rational) =
  //前者this可以去掉，后者的this不可以去掉，否则返回为空
    if (this.lessThan(that)) that else this

  private def gcd(a:Int,b:Int): Int = {
    if(b == 0) a else gcd(b,a % b)
  }
}
