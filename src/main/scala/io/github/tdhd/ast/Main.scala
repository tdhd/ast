package io.github.tdhd
package ast

import _root_.io.github.tdhd.ast.scoring.Scorer

object Main {

  def main(args: Array[String]): Unit = {

    val f = io.Loader.loadFile("src/test/resources/code/Test.scala")
//
//    // todo: given a seq of source files, calculate all pairwise similarities
//
//    val tests = io.Loader.sourceFilesFor(io.Loader.testRoot)
//
//    val f1 = tests.head.functions(2)
//    val f2 = tests.head.functions(3)
//
//    println(f1.structure)
//    println(f1.tokens)
//    println(f1.syntax)
//
//    // given root dir, load all files and all functions
//
//    println(f1)
//    println(f2)
//
//    println(kernel.ConvNLP(f1, f2, lambda = 0.01))

    val scores = Scorer.functionSimilaritiesFrom(f.functions)
    scores.map {
      case ((a, b), s) ⇒ (a.name, b.name, s)
    }.toSeq.sortBy(-_._3).foreach(println)
  }
}
