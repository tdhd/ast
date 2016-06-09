package io.github.tdhd.ast.parser

import scala.reflect.internal.{Names, Trees}
import scala.reflect.runtime.universe._

object TreeParser {

  case class DefDef(
    mods: Trees#Modifiers, tname: Names#TermName, tparams: Seq[Trees#TypeDef],
    paramss: Seq[Trees#ValDef], tpt: Trees#Tree, expr: Trees#Tree
  )

  def defdefFrom(tree: Tree): Seq[DefDef] = {
    tree collect { case q"$mods def $tname[..$tparams](...$paramss): $tpt = $expr" ⇒
      val a = mods.asInstanceOf[scala.reflect.internal.Trees#Modifiers]
      val b = tname.asInstanceOf[scala.reflect.internal.Names#TermName]
      val c = tparams.asInstanceOf[Seq[scala.reflect.internal.Trees#TypeDef]]
      val d = paramss.asInstanceOf[Seq[scala.reflect.internal.Trees#ValDef]]
      val e = tpt.asInstanceOf[scala.reflect.internal.Trees#Tree]
      val f = expr.asInstanceOf[Trees#Tree]

      DefDef(a, b, c, d, e, f)
    }
  }

}
