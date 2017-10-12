package io.github.tdhd.ast.graph


import org.scalatest.{Inspectors, Matchers, WordSpec}

import scalax.collection.Graph

class GraphAugTest extends WordSpec with Matchers with Inspectors {

  import io.github.tdhd.ast.graph.GraphAug

  "Graph augmentation" should {

    case class House(name: String) extends Ordered[House] {
      override def compare(that: House): Int =
        name.compareTo(that.name)
    }

    "compute adjacency matrix" in {
      import scalax.collection.edge.Implicits._
      val graph = {
        val A = House(name = "A")
        val B = House(name = "B")
        val C = House(name = "C")
        val D = House(name = "D")

        Graph((A ~+> B)("ab"), (A ~+> C)("ac"), (B ~+> C)("bc"), (C ~+> D)("cd"))
      }

      val A = graph.adjacencyMatrix
      A.activeValuesIterator.toList shouldBe List(
        0, 1, 1, 0,
        0, 0, 1, 0,
        0, 0, 0, 1,
        0, 0, 0, 0
      )
    }

    "convert graph to DOT format" in {
      import scalax.collection.edge.Implicits._
      val graph = Graph((House("1")~+>House("2"))("12"))

      graph.toDotFormat shouldBe
        """digraph test {
          |	"House(1)" -> "House(2)" [label = 12]
          |}""".stripMargin

    }
  }
}
