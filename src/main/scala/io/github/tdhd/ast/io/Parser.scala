package io.github.tdhd
package ast
package io

import scala.meta._

object Parser {

  def valsOf(source: String): Seq[scala.meta.Defn.Val] =
    source.parse[Source].get.collect {
      case q"..$mods val ..$patsnel: $tpeopt = $expr" ⇒
        scala.meta.Defn.Val(mods, patsnel, tpeopt, expr)
    }

  def functionsOf(source: String): Seq[scala.meta.Defn.Def] =
    source.parse[Source].get.collect {
      case q"..$mods def $tname[..$tparams](...$paramss): $tpe = $ex" ⇒
        scala.meta.Defn.Def(mods, tname, tparams, paramss, tpe, ex)
    }
}
