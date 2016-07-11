package io.github.tdhd
package ast
package io

import org.scalatest.{Inspectors, Matchers, WordSpec}

class ParserTest extends WordSpec with Matchers with Inspectors {

  import ParserTest._

  "Parser" should {
    "parse all function bodies" in {
      val functions = Parser.functionsOf(src)

      functions should have size 3

      forAll(functions) { function ⇒
        function.children should have size 2
      }
    }

    "parse all vals" in {
      Parser.valsOf(src) should have size 1
    }
  }

}

object ParserTest {
  val src =
    """
      |class A {
      |  def a = 1
      |  def b = 2
      |  def c = 3
      |
      |  val some = 123
      |}
    """.stripMargin
}