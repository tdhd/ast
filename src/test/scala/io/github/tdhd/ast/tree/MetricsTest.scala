package io.github.tdhd
package ast
package tree

import org.scalatest.{Inspectors, Matchers, WordSpec}

class MetricsTest extends WordSpec with Matchers with Inspectors {

  import MetricsTest._

  "tree metrics" should {
    "calculate tree size" in { Metrics.size(tree) shouldBe 4 }
    "calculate tree depth" in { Metrics.depth(tree) shouldBe 2 }
  }
}

object MetricsTest {

  val rhs = scala.meta.Term.Name("rhs")
  val arg = scala.meta.Term.Name("pat")
  val args = scala.meta.Term.Name("1") :: scala.meta.Term.Name("2") :: Nil

  val tree = scala.meta.Defn.Val(
    mods = scala.meta.Mod.Override() :: Nil,
    pats = scala.meta.Pat.Var.Term(arg) :: Nil,
    decltpe = None,
    rhs = scala.meta.Term.Apply(rhs, args)
  )

}
