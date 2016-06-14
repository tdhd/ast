package io.github.tdhd.ast.kernel

import org.scalatest.{Inspectors, Matchers, WordSpec}

import scala.reflect.internal.Trees

class ConvNLPTest extends WordSpec with Matchers with Inspectors {

  import ConvNLPTest._

  "kernel" should {
    "yield 1 for identical trees" in {
      ConvNLP(first, first, lambda = lambda) shouldBe 1.0
    }

    "yield [0,1] for some trees" in {
      ConvNLP(first, second, lambda = lambda) shouldBe 0.5 +- 0.5
    }
  }

}

object ConvNLPTest {
  import scala.reflect.runtime.universe._
  val lambda = 0.01
  val first = {
    val s = Select(Apply(Select(Ident(TermName("numbers")), TermName("map")), List(Function(List(ValDef(Modifiers(), TermName("x$1"), TypeTree(), EmptyTree)), Apply(Select(Ident(TermName("x$1")), TermName("$div")), List(Literal(Constant(2))))))), TermName("headOption"))
    s.asInstanceOf[Trees#Tree]
  }

  val second = {
    val s = Block(List(ValDef(Modifiers(), TermName("f"), TypeTree(), Apply(Select(Ident(TermName("Future")), TermName("successful")), List(Literal(Constant("123")))))), Apply(Select(Ident(TermName("f")), TermName("flatMap")), List(Function(List(ValDef(Modifiers(), TermName("string"), TypeTree(), EmptyTree)), Apply(Select(Match(Ident(TermName("string")), List(CaseDef(Literal(Constant("a")), EmptyTree, Apply(Select(Ident(TermName("Future")), TermName("failed")), List(Apply(Select(New(Ident(TypeName("RuntimeException"))), termNames.CONSTRUCTOR), List(Literal(Constant("[TEST]"))))))), CaseDef(Literal(Constant("b")), EmptyTree, Apply(Select(Ident(TermName("Future")), TermName("successful")), List(Literal(Constant("321"))))))), TermName("map")), List(Function(List(ValDef(Modifiers(), TermName("another"), TypeTree(), EmptyTree)), Ident(TermName("another")))))))))
    s.asInstanceOf[Trees#Tree]
  }
}
