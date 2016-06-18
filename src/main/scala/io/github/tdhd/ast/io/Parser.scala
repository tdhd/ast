package io.github.tdhd
package ast
package io

import scala.meta._

object Parser {

  def valsOf(source: String): Seq[Tree] =
    source.parse[Source].get.collect {
      case q"..$mods val ..$patsnel: $tpeopt = $expr" ⇒
        scala.meta.Defn.Val(mods, patsnel, tpeopt, expr)
    }

  def functionsOf(source: String): Seq[Tree] =
    source.parse[Source].get.collect {
      case q"..$mods def $tname[..$tparams](...$paramss): $tpe = $ex" ⇒
        scala.meta.Defn.Def(mods, tname, tparams, paramss, tpe, ex)
    }
}
