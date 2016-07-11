package io.github.tdhd
package ast

object Main {

  // todo: move to package
  def topSimsFor(tree: scala.meta.Tree, trees: Seq[scala.meta.Tree]): Seq[(scala.meta.Tree, Double)] =
    trees.map { case t ⇒
      (t, kernel.ConvNLP(tree, t, 0.03))
    }.sortBy(-_._2).take(5)

  def main(args: Array[String]): Unit = {

    val f = io.Loader.loadFile("src/test/resources/code/Test.scala")

//    val (nodes, edges) = translation.GraphBuilder(f.functions.drop(2).head)
//    println(nodes)
//    println(edges)

    val K = for {
      first ← f.functions
    } yield f.functions.map(kernel.ConvNLP(first, _, 0.015))

    println(f.functions.map(e ⇒ s"'${e.name}'").mkString(","))
    println(s"${f.functions.size} functions in 3-space")
    K.foreach(p ⇒ println(s"""[${p.map(n ⇒ f"$n%1.2f").mkString(",")}],"""))

    println("Tree sizes")
    f.functions.map(e ⇒ (e.name, tree.Metrics.size(e))).sortBy(_._2).foreach(println)
    println("Tree depths")
    f.functions.map(e ⇒ (e.name, tree.Metrics.depth(e))).sortBy(_._2).foreach(println)

    val first = f.functions.drop(2).head
    val remaining = f.functions diff Seq(first)
    println(s"Top 5 similarities for $first:")
    topSimsFor(first, remaining).foreach(println)
  }
}
