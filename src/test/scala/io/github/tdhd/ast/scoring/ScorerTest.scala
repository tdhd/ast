package io.github.tdhd.ast.scoring

import io.github.tdhd.ast.io
import org.scalatest.{Inspectors, Matchers, WordSpec}

class ScorerTest extends WordSpec with Matchers with Inspectors {

  import ScorerTest._

  "scorer" should {
    "return the correct number of entries" in {
      Scorer.functionSimilaritiesFrom(functions) should have size functions.size * functions.size
    }

    "calculate function similarities correctly" in {
      val sims = Scorer.functionSimilaritiesFrom(first :: Nil)
      forAll(sims) { sim ⇒
        sim._2 shouldBe 1.0
      }
    }
  }
}

object ScorerTest {

  val functions = io.Loader.sourceFilesFor(io.Loader.testRoot).head.functions.take(4)
  val first = functions.head

}
