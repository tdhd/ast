package a.b.c

sealed trait BaseTrait

class Test extends BaseTrait {
  def someMethod =
    (1 to 5).map(_ * 2).headOption

  def someOther =
    (1 to 10).map(_ / 2).headOption
}