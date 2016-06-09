package io.github.tdhd.ast.compiler

import java.io.File

import org.scalatest.{Inspectors, Matchers, WordSpec}

class JitTest extends WordSpec with Matchers with Inspectors {

  import JitTest._

  "Jit compiler" should {
    "parse AST from source" in {
      val srcTree = Jit.treeFrom(src)
      srcTree.children should have size 4
    }
  }

}

object JitTest {

  val file = new File("src/test/resources/code/Test.scala")
  val src = scala.io.Source.fromFile(file).mkString.replaceAll("package", "//package")

}
