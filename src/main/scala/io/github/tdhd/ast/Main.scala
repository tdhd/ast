package io.github.tdhd
package ast

object Main {

  def main(args: Array[String]): Unit = {

    val f = io.Loader.loadFile("src/test/resources/code/Test.scala")

    val (nodes, edges) = translation.GraphBuilder(f.functions.drop(2).head)

    println(nodes)
    println(edges)

    val K = for {
      first ← f.functions
    } yield f.functions.map(kernel.ConvNLP(first, _, 0.015))

    println(f.functions.map(e ⇒ s"'${e.name}'").mkString(","))
    println(s"${f.functions.size} functions in 3-space")
    K.foreach(p ⇒ println(s"""[${p.map(n ⇒ f"$n%1.2f").mkString(",")}],"""))
  }
}
