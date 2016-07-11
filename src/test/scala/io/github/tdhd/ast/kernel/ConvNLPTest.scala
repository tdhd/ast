package io.github.tdhd
package ast
package kernel

import org.scalatest.{Inspectors, Matchers, WordSpec}

import scala.meta._

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
      similarityOf(literalConstant, termName) shouldBe 0.0
    }

    "yield small similarity for constant in big tree" in new ConvNLPScope {
      similarityOf(literalConstant, big) shouldBe 0.11 +- delta
    }

    "yield small similarity for constant in small tree" in new ConvNLPScope {
      similarityOf(literalConstant, small) shouldBe 0.13 +- delta
    }

    "yield bigger similarity for term name in method with many term names" in new ConvNLPScope {
      similarityOf(termName, small) should be >
        similarityOf(termName, manyConstantsAndTerms)
    }
  }

  "parser" should {
    "retrieve all nodes from the tree" in {
      ConvNLP.allNodesOf(small) should have size smallSize
    }
  }

  class ConvNLPScope {
    def similarityOf(f: scala.meta.Tree, s: scala.meta.Tree) = ConvNLP(f, s, lambda)
  }

}

object ConvNLPTest {

  val lambda = 0.01
  val delta = 0.01
  val smallDelta = 1e-6

  val bodies = io.Loader.sourceFilesFor(io.Loader.testRoot).head.functions

  val smallSize = 19
  val small = bodies(1)
  val big = bodies(4)
  val manyConstantsAndTerms = bodies(3)
  val (literalConstant, termName) = (Lit(3), Term.Name("a"))
}
