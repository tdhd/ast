package io.github.tdhd
package ast
package io

import scala.meta._

// todo: tests!

object Parser {

  // todo: generalize
  def parse(file: Loader.LoaderSourceFile): Seq[scala.meta.Tree] = {
    file.parsedSource.get.collect {
      case q"..$mods def $tname[..$tparams](...$paramss): $tpe = $ex" ⇒
        scala.meta.Defn.Def(mods, tname, tparams, paramss, tpe, ex)
    }
  }
}
