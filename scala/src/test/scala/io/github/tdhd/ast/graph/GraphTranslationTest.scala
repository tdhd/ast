package io.github.tdhd.ast.graph

import io.github.tdhd.ast.io.Parser
import org.scalatest.{Inspectors, Matchers, WordSpec}

class GraphTranslationTest extends WordSpec with Matchers with Inspectors {

  "Graph builder" should {

    "build scalax.collection.Graph from scala.meta.Tree" in {
      val tree = Parser.treeFrom(
        """
          |object A {
          | def a(b: Int, c: String): String =
          |   if (1 > 0) "1"
          |   else c.toString
          |}
        """.stripMargin
      )

      val graph = io.github.tdhd.ast.graph.directedScalaMetaTypeGraphFrom(tree)

      graph.nodes should have size 12
      graph.edges should have size 18
    }
  }
}
