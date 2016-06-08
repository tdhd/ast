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
        source.functions should have size 3

        forExactly(1, source.functions) { fn ⇒
          showRaw(fn) shouldBe rawFn
        }

      }
    }
  }

}

object LoaderTest {

  val testRoot = new File("src/test/resources")
  val rawFn = """Select(Apply(Select(Apply(Select(Literal(Constant(1)), TermName("to")), List(Literal(Constant(5)))), TermName("map")), List(Function(List(ValDef(Modifiers(PARAM | SYNTHETIC), TermName("x$1"), TypeTree(), EmptyTree)), Apply(Select(Ident(TermName("x$1")), TermName("$times")), List(Literal(Constant(2))))))), TermName("headOption"))"""

}
