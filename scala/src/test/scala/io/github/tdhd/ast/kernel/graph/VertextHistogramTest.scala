package io.github.tdhd.ast.kernel.graph


import org.scalatest.{Inspectors, Matchers, WordSpec}

import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.edge.Implicits._

class VertextHistogramTest extends WordSpec with Matchers with Inspectors {

  "Vertex Histogram kernel" should {

    case class House(id: Int) extends Ordered[House] {
      override def compare(that: House): Int =
        id.compareTo(that.id)
    }

    "be symmetric" in {
      val first = Graph((House(1)~+>House(2))("12"), (House(2)~+>House(3))("23"))
      val second = Graph((House(1)~+>House(2))("12"))

      VertexHistogram(first, second) shouldBe VertexHistogram(second, first)
    }

    "yield 1 for same graphs" in {
      val graph = Graph((House(1)~+>House(2))("12"))
      VertexHistogram(graph, graph) shouldBe 1.0 +- 1e-3
    }

    "yield 0 for maximally dissimilar graphs" in {
      val first = Graph((House(1)~+>House(2))("12"))
      val second = Graph((House(3)~+>House(4))("34"))
      VertexHistogram(first, second) shouldBe 0.0
    }
  }
}
