package io.github.tdhd.ast.kernel.graph

import io.github.tdhd.ast.io.Parser
import org.scalatest.{Inspectors, Matchers, WordSpec}

import scalax.collection.Graph
import scalax.collection.GraphPredef._

class RandomWalkTest extends WordSpec with Matchers with Inspectors {

  "Random Walk kernel" should {
    import scalax.collection.edge.Implicits._

    val lambda = 0.05

    case class House(number: Int) extends Ordered[House] {
      override def compare(that: House): Int =
        number.compareTo(that.number)
    }

    "be symmetric" in {
      val first = Graph((House(1)~+>House(2))("12"), (House(2)~+>House(3))("23"))
      val second = Graph((House(1)~+>House(2))("13"))

      RandomWalk(first, second, lambda) shouldBe RandomWalk(second, first, lambda)
    }

    "yield 0 for maximally dissimilar graphs" in {
      val first = Graph((House(1)~+>House(2))("12"))
      val second = Graph((House(3)~+>House(4))("34"))
      RandomWalk(first, second, lambda) shouldBe 0.0
    }
  }
}
