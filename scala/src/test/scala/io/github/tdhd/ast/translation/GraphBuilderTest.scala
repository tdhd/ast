package io.github.tdhd
package ast
package translation

import org.scalatest.{Inspectors, Matchers, WordSpec}

class GraphBuilderTest extends WordSpec with Matchers with Inspectors {

  import GraphBuilderTest._

  "graph builder" should {
    "return the correct number of entries" in {
      val (nodes, edges) = GraphBuilder(tree)
      nodes should have size 24
      edges should have size 23
    }
  }
}

object GraphBuilderTest {

  val tree = io.Loader.sourceFilesFor(io.Loader.testRoot).head.functions.last

}
