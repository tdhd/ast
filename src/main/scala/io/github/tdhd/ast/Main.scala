package io.github.tdhd
package ast

import io.Loader

import scala.reflect.runtime.universe._

object Main {

  def main(args: Array[String]): Unit = {
    val sourceFileDefs = Loader.functionBodiesFor(Loader.testRoot)

    println("---"*10)
    println(showRaw(sourceFileDefs.head.functions(3)))
    println("---"*10)

    println("-"*100)

    val similarities = for {
      f ← sourceFileDefs.head.functions
      s ← sourceFileDefs.head.functions
    } yield s"""K(${f.tname}, ${s.tname}): ${kernel.ConvNLP(f.expr, s.expr, 0.01)}"""

    similarities.foreach(println)
  }
}
