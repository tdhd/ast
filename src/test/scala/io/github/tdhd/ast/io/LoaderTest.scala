package io.github.tdhd.ast.io

import org.scalatest.{Inspectors, Matchers, WordSpec}

class LoaderTest extends WordSpec with Matchers with Inspectors {

  import LoaderTest._

  "Loader" should {
    "load all functions trees for all sources" in {
      val sources = Loader.sourceFilesFor(Loader.testRoot)

      sources should have size 1

      forAll(sources) { source ⇒
        source.functions should not be empty
      }
    }

    "load one source file" in {
      val f = Loader.loadFile(testFilePath)

      f.functions should have size 11
    }
  }

}

object LoaderTest {
  val testFilePath = "src/test/resources/code/Test.scala"
}