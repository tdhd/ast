package io.github.tdhd
package ast

import io.Loader

object Main {

  def main(args: Array[String]): Unit = {
    val sourceFileDefs = Loader.functionBodiesFor(Loader.testRoot)

    for {
      file ← sourceFileDefs
    } yield {

      println(
        s"""
           |${file.file.file.getAbsolutePath} has ${file.functions.size} functions:
           |${file.functions.map(d ⇒ (d.mods, d.tname, d.paramss, d.expr)).mkString("\n")}""".stripMargin
      )
    }

    val first = sourceFileDefs.head.functions(2)
    val second = sourceFileDefs.head.functions(3)

    import scala.reflect.runtime.universe._
    println("---"*10)
    println(showRaw(sourceFileDefs.head.functions(3)))
    println("---"*10)

    val lambda = 0.01
    val sim = kernel.ConvNLP(first.expr, second.expr, lambda)
    println(sim)

//    val kernels = for {
//      ta ← sourceFileDefs
//      tb ← sourceFileDefs
//      _ = println(s"a ${ta.file.file} has ${ta.functions.size} function definitions")
//      _ = println(s"b ${tb.file.file} has ${tb.functions.size} function definitions")
//    } yield new kernel.ConvNLP(ta.functions.head.expr, tb.functions.head.expr)
//
//    kernels.map(_.similarity).foreach(println)
  }
}
