package io.github.tdhd.ast.parser

import scala.reflect.internal.Trees
import scala.reflect.runtime.universe._

object TreeParser {

  def defDefExprsFrom(tree: Tree): Seq[Trees#Tree] = {
    tree collect { case q"$mods def $tname[..$tparams](...$paramss): $tpt = $expr" ⇒
      // todo
      expr.asInstanceOf[Trees#Tree]
    }
  }

}
