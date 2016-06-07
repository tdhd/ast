package io.github.tdhd
package ast

import io.Loader

object Main {
  val universe = scala.reflect.runtime.universe

  import universe._

  def main(args: Array[String]): Unit = {

//    Loader.allFiles(Loader.testRoot).foreach(println)

    val f = Loader.allFiles(Loader.testRoot).last
    println(s"opening ${f._1}")
    val tree = compiler.Jit.treeFrom(f._2)

    val res = tree.collect {
      case q"$mods def $tname[..$tparams](...$paramss): $tpt = $expr" ⇒
        println(expr)
        // todo: runtime
        expr.asInstanceOf[scala.reflect.internal.Trees#Tree]
    }

    val k = new kernel.ConvNLP(res.drop(1).head, res.drop(2).head)
    println(k.similarity)
  }
}
