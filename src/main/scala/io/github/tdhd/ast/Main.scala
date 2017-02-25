package io.github.tdhd
package ast

import breeze.linalg.DenseMatrix

object Main {

    // todo: functions-within-file similarities
    def main(args: Array[String]): Unit = {
      import ast.graph.GraphAug
      val basepath = "/home/thread/workspace/ast/src/test/resources/code/"
      val sources = io.Loader.sourceFilesFor(new java.io.File(basepath))
      val filenames = sources.map(_.file.toString.replace(basepath, "").replace("/", "."))

      val functionTrees = sources.flatMap { s ⇒
        s.functions.map { fn ⇒
          fn.name.toString → graph.directedScalaMetaTypeGraphFrom(fn)
        }
      }

      val n = functionTrees.size

      import kernel.graph.VertexHistogram
      val ks = functionTrees.flatMap { g1 ⇒
        functionTrees.map { g2 ⇒
          val k = VertexHistogram(g1._2, g2._2)
//          "%02d".format(k.toInt)
          "%02.2f".format(k)
        }
      }
      val m = new DenseMatrix(rows = n, cols = n, data = ks.toArray)
      val km = m.toString(maxLines = n*20, maxWidth = n*20).replace("  ", ",")
      scala.tools.nsc.io.File(s"/tmp/km.csv").writeAll(
        s"""
           |${functionTrees.map(_._1).mkString(",")}
           |$km
         """.stripMargin)
    }

//  // todo: whole-file similarities
//  def main(args: Array[String]): Unit = {
//    import ast.graph.GraphAug
//    val basepath = "/home/thread/workspace/skuber/"
//    val n = 50
//    val sources = io.Loader.sourceFilesFor(new java.io.File(basepath)).take(n)
//    val filenames = sources.map(_.file.toString.replace(basepath, "").replace("/", "."))
//    val trees = sources.map(_.source).map(io.Parser.treeFrom)
//    val graphs = trees.map(graph.directedScalaMetaTypeGraphFrom)
////    val dots = graphs.map(_.toDotFormat)
////    dots.zip(filenames).foreach { case (dot, fn) ⇒
////      scala.tools.nsc.io.File(s"/tmp/${fn}.dot").writeAll(dot)
////    }
//    import kernel.graph.RandomWalk
//    val ks = graphs.flatMap { g1 ⇒
//      graphs.map { g2 ⇒
//        val k = RandomWalk(g1, g2, lambda = 0.05)
//        "%02d".format(k.toInt)
//      }
//    }
//    val m = new DenseMatrix(rows = n, cols = n, data = ks.toArray)
//    val km = m.toString(maxLines = n*20, maxWidth = n*20).replace("  ", ",")
//    scala.tools.nsc.io.File(s"/tmp/km.csv").writeAll(
//      s"""
//         |${filenames.mkString(",")}
//         |${km.dropRight(1)}
//       """.stripMargin)
//  }

//  // todo: move to package
//  def topSimsFor(tree: scala.meta.Tree, trees: Seq[scala.meta.Tree]): Seq[(scala.meta.Tree, Double)] =
//    trees.map { t ⇒
//      (t, kernel.ConvNLP(tree, t, 0.03))
//    }.sortBy(-_._2).take(5)

//  def main(args: Array[String]): Unit = {
//
//    val f = io.Loader.loadFile("src/test/resources/code/Test.scala")
//
////    val (nodes, edges) = translation.GraphBuilder(f.functions.drop(2).head)
////    println(nodes)
////    println(edges)
//
//    val K = for {
//      first ← f.functions
//    } yield f.functions.map(kernel.ConvNLP(first, _, 0.015))
//
//    println(f.functions.map(e ⇒ s"'${e.name}'").mkString(","))
//    println(s"${f.functions.size} functions in 3-space")
//    K.foreach(p ⇒ println(s"""[${p.map(n ⇒ f"$n%1.2f").mkString(",")}],"""))
//
//    println("Tree sizes")
//    f.functions.map(e ⇒ (e.name, tree.Metrics.size(e))).sortBy(_._2).foreach(println)
//    println("Tree depths")
//    f.functions.map(e ⇒ (e.name, tree.Metrics.depth(e))).sortBy(_._2).foreach(println)
//
//    val first = f.functions.drop(2).head
//    val remaining = f.functions diff Seq(first)
//    println(s"Top 5 similarities for $first:")
//    topSimsFor(first, remaining).foreach(println)
//  }
}
