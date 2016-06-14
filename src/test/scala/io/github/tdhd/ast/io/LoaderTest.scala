package io.github.tdhd.ast.io

import java.io.File

import org.scalatest.{Inspectors, Matchers, WordSpec}

import scala.reflect.runtime.universe._

class LoaderTest extends WordSpec with Matchers with Inspectors {

  import LoaderTest._

  "Loader" should {
    "load all functions trees for all sources" in {
      val sources = Loader.functionBodiesFor(testRoot)

      sources should have size 1

      forAll(sources) { source ⇒
        source.functions should have size 12

        forExactly(1, source.functions) { fn ⇒
          showRaw(fn.expr) shouldBe rawFn
        }

      }
    }
  }

}

object LoaderTest {

  val testRoot = new File("src/test/resources")
  val rawFn = """Apply(Ident(TermName("Some")), List(Ident(TermName("p"))))"""

}
