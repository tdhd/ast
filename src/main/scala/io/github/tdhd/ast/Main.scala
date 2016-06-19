package io.github.tdhd
package ast

object Main {

  def main(args: Array[String]): Unit = {

    val f = io.Loader.loadFile("src/test/resources/code/Test.scala")

    val (nodes, edges) = translation.GraphBuilder(f.functions.drop(2).head)

    println(nodes)
    println(edges)

    val similarities = scoring.Scorer.functionSimilaritiesFrom(f.functions)

    println(similarities)
  }
}
