package io.github.tdhd.ast.io

import org.scalatest.{Inspectors, Matchers, WordSpec}

class LoaderTest extends WordSpec with Matchers with Inspectors {

  "Loader" should {
    "load all functions trees for all sources" in {
      val sources = Loader.sourceFilesFor(Loader.testRoot)

      sources should have size 1

      forAll(sources) { source ⇒
        source.parsedSource.get.parent shouldBe empty
      }
    }
  }

}
