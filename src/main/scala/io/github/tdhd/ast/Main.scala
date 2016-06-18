package io.github.tdhd
package ast

object Main {

  def main(args: Array[String]): Unit = {

    // todo: given a seq of source files, calculate all pairwise similarities
    val tests = io.Loader.sourceFilesFor(io.Loader.testRoot)
    // todo: use this
    val result = tests.map(io.Parser.functionsOf)
    val bodies = tests.head.functions

//    bodies.map(_.structure).foreach(println)
//    bodies.map(_.tokens).foreach(println)
//    bodies.map(_.syntax).foreach(println)

    // given root dir, load all files and all functions

    println(bodies(2))
    println(bodies(3))

    println(kernel.ConvNLP(bodies(2), bodies(3), lambda = 0.01))
  }
}
