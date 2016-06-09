package a.b.c

import scala.concurrent.Future

sealed trait BaseTrait

class Test extends BaseTrait {
  def someMethod[T <: String](p: T): Option[T] =
    Some(p)

  protected def someOther(numbers: Seq[Int]) =
    numbers.map(_ / 2).headOption

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
}