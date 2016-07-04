package io.github.tdhd
package ast

object Main {

  def main(args: Array[String]): Unit = {

    val f = io.Loader.loadFile("src/test/resources/code/Test.scala")

    val (nodes, edges) = translation.GraphBuilder(f.functions.drop(2).head)

    println(nodes)
    println(edges)

    val similarities = scoring.Scorer.functionSimilaritiesFrom(f.functions)

    similarities.map {
      case ((f, s), k) ⇒ ((f.name, s.name), k)
    }.foreach(println)

    val K = for {
      first ← f.functions
    } yield f.functions.map(kernel.ConvNLP(first, _, 0.04))

    println(s"${f.functions.size} functions in 3-space")
    K.foreach(p ⇒ println(s"""[${p.mkString(",")}],"""))
  }
}
