package io.github.tdhd
package ast

import io.Loader

object Main {

  def main(args: Array[String]): Unit = {
    val functionTrees = Loader.functionBodiesFor(Loader.testRoot)

    val kernels = for {
      ta ← functionTrees
      tb ← functionTrees
      _ = println(s"a ${ta.file.file} has ${ta.functions.size} function definitions")
      _ = println(s"b ${tb.file.file} has ${tb.functions.size} function definitions")
    } yield new kernel.ConvNLP(ta.functions.head, tb.functions.head)

    kernels.map(_.similarity).foreach(println)
  }
}
