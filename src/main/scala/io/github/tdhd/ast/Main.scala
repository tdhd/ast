package io.github.tdhd
package ast

import scala.meta._

object Main {

  def main(args: Array[String]): Unit = {

    // todo: given a seq of source files, calculate all pairwise similarities
    val tests = io.Loader.sourceFilesFor(io.Loader.testRoot)
    // todo: use this
    val result = tests.map(io.Parser.parse)
    val bodies = tests.head.functions

//    bodies.map(_.structure).foreach(println)
//    bodies.map(_.tokens).foreach(println)
//    bodies.map(_.syntax).foreach(println)

    println(bodies(2))
    println(bodies(3))

    val sim = kernel.ConvNLP(bodies(2), bodies(3), lambda = 0.01)
    println(sim)
  }
}
