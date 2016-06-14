package io.github.tdhd.ast.kernel

import org.scalatest.{Inspectors, Matchers, WordSpec}

import scala.reflect.runtime.universe._

class ConvNLPTest extends WordSpec with Matchers with Inspectors {

  import ConvNLPTest._

  "kernel" should {
    "be symmetric" in new ConvNLPScope {
      val difference = similarityOf(small, big) - similarityOf(big, small)
      difference shouldBe 0.0 +- smallDelta
    }

    "yield 1 for identical trees" in new ConvNLPScope {
      similarityOf(small, small) shouldBe 1.0
    }

    "yield [0,1] for some trees" in new ConvNLPScope {
      similarityOf(small, big) shouldBe 0.5 +- 0.5
    }

    "yield 0 for completely different trees" in new ConvNLPScope {
      similarityOf(literalConstant, identTermName) shouldBe 0.0
    }

    "yield small similarity for constant in big tree" in new ConvNLPScope {
      similarityOf(literalConstant, big) shouldBe 0.35 +- delta
    }

    "yield small similarity for constant in small tree" in new ConvNLPScope {
      similarityOf(literalConstant, small) shouldBe 0.21 +- delta
    }

    "yield bigger similarity for term name in method with many term names" in new ConvNLPScope {
      similarityOf(identTermName, manyConstantsAndTerms) should be >
        similarityOf(identTermName, small)
    }
  }

  "parser" should {
    "retrieve all nodes from the tree" in {
      ConvNLP.allNodesOf(small) should have size smallSize
    }
  }

  class ConvNLPScope {
    def similarityOf(f: scala.reflect.internal.Trees#Tree, s: scala.reflect.internal.Trees#Tree) =
      ConvNLP(f, s, lambda)
  }

}

object ConvNLPTest {

  implicit class TreeAug(t: scala.reflect.api.Trees#Tree) {
    def internalFromApi = t.asInstanceOf[scala.reflect.internal.Trees#Tree]
  }

  val lambda = 0.01
  val delta = 0.01
  val smallDelta = 1e-6

  val smallSize = 11
  val small =
    Select(Apply(Select(Ident(TermName("numbers")), TermName("map")), List(Function(List(ValDef(Modifiers(), TermName("x$1"), TypeTree(), EmptyTree)), Apply(Select(Ident(TermName("x$1")), TermName("$div")), List(Literal(Constant(2))))))), TermName("headOption"))
      .internalFromApi

  val big =
    Block(List(ValDef(Modifiers(), TermName("f"), TypeTree(), Apply(Select(Ident(TermName("Future")), TermName("successful")), List(Literal(Constant("123")))))), Apply(Select(Ident(TermName("f")), TermName("flatMap")), List(Function(List(ValDef(Modifiers(), TermName("string"), TypeTree(), EmptyTree)), Apply(Select(Match(Ident(TermName("string")), List(CaseDef(Literal(Constant("a")), EmptyTree, Apply(Select(Ident(TermName("Future")), TermName("failed")), List(Apply(Select(New(Ident(TypeName("RuntimeException"))), termNames.CONSTRUCTOR), List(Literal(Constant("[TEST]"))))))), CaseDef(Literal(Constant("b")), EmptyTree, Apply(Select(Ident(TermName("Future")), TermName("successful")), List(Literal(Constant("321"))))))), TermName("map")), List(Function(List(ValDef(Modifiers(), TermName("another"), TypeTree(), EmptyTree)), Ident(TermName("another")))))))))
      .internalFromApi

  val manyConstantsAndTerms =
    DefDef(Modifiers(), TermName("some"), List(), List(List(ValDef(Modifiers(), TermName("numbers"), AppliedTypeTree(Ident(TypeName("Seq")), List(Ident(TypeName("Int")))), EmptyTree))), TypeTree(), Block(List(ValDef(Modifiers(), TermName("a"), TypeTree(), Literal(Constant(1))), ValDef(Modifiers(), TermName("b"), TypeTree(), Literal(Constant(2))), ValDef(Modifiers(), TermName("c"), TypeTree(), Literal(Constant(3)))), Apply(Select(Apply(Select(Ident(TermName("a")), TermName("$plus")), List(Ident(TermName("b")))), TermName("$plus")), List(Ident(TermName("c"))))))
      .internalFromApi

  val (literalConstant, identTermName) = (
    Literal(Constant(2)).internalFromApi,
    Ident(TermName("x$1")).internalFromApi
  )
}
