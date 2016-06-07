package io.github.tdhd.ast

import compiler.Jit

object TreeParser {
  val universe = scala.reflect.runtime.universe

  import universe._

  def defDefExprsFrom(tree: Jit.universe.Tree): Seq[scala.reflect.internal.Trees#Tree] = {
    tree collect { case q"$mods def $tname[..$tparams](...$paramss): $tpt = $expr" ⇒
      expr.asInstanceOf[scala.reflect.internal.Trees#Tree]
    }
  }

}
