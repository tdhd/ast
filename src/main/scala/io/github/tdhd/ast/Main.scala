package io.github.tdhd
package ast

import kernel.ConvNLP
import io.Loader

object Main {
  val universe = scala.reflect.runtime.universe
  import universe._

  def main(args: Array[String]): Unit = {

    Loader.allFiles.foreach(println)

    val tree = compiler.Jit.toolbox.parse(Loader.allFiles.head)
    tree.filter {
      case q"$mods def $tname[..$tparams](...$paramss): $tpt = $expr" ⇒
        println(expr)
        // todo fix types
        val k = new ConvNLP(???, ???)

        true
      case _ ⇒ false
    }
  }
}
