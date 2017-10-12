package io.github.tdhd
package ast
package io

import scala.meta._

object Parser {

  def treeFrom(source: String): scala.meta.Tree =
    source.parse[Source].get

  def valsOf(source: String): Seq[scala.meta.Defn.Val] =
    source.parse[Source].get.collect {
      case q"..$mods val ..$patsnel: $tpeopt = $expr" ⇒
        scala.meta.Defn.Val(mods, patsnel, tpeopt, expr)
    }

  def functionsOf(source: String): Seq[scala.meta.Defn.Def] =
    source.parse[Source].get.collect {
      case d: scala.meta.Defn.Def ⇒ d
    }
}
