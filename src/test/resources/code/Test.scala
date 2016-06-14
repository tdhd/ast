package a.b.c

import scala.concurrent.Future
import scala.util.Try

sealed trait BaseTrait

class Test extends BaseTrait {
  def someMethod[T <: String](p: T): Option[T] =
    Some(p)

  protected def someOther(numbers: Seq[Int]) =
    numbers.map(_ / 2).headOption

  protected def someOther2(numbers: Seq[Int]) =
    numbers.map(_ / 2).filter(_ == 1).headOption

  protected def someOther3(numbers: Seq[Int]) =
    numbers.map(_ / 2).find(_ == 1)

  protected def someOther4(numbers: Seq[Int]) = {
    def inner(a: Int) = Future.successful(a)
    numbers.map(_ / 2).filter(inner(_) == 0).headOption
  }

  private[this] def calcInts(numbers: Seq[Int]) = {
    val f = Future.successful("123")
    for {
      string ← f
      another ← string match {
        case "a" ⇒ Future.failed(new RuntimeException("[TEST]"))
        case "b" ⇒ Future.successful("321")
      }
    } yield another
  }

  def rec(e: Seq[Double]): Seq[Double] = e match {
    case Nil ⇒ e
    case elem :: tail ⇒ Seq(elem) ++ rec(tail)
  }

  def rec2(e: Seq[Double]): Seq[Double] = e match {
    case Nil ⇒ e
    case elem :: tail ⇒ Seq(Try(math.sqrt(elem / 2.0d)).toOption.get) ++ rec(tail)
  }

  def defs1 = {
    val a = Seq(1, 2, 3)
    a map (_ * 3)
  }

  def defs2 = {
    val a = Seq("a", "b", "c")
    a map (_ * 3) filter (_ == "aaa")
  }
}