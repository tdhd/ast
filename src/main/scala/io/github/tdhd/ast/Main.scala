package io.github.tdhd
package ast

import java.io.File

import _root_.io.github.tdhd.ast.io.Loader.LoadedFile
import io.Loader

import scala.reflect.internal.Trees

object Main {

  case class Source(file: LoadedFile, functions: Seq[Trees#Tree])

  def loadFunctionTrees(root: File): Seq[Source] = {
    for {
      file ← Loader.allFiles(root)
      tree = compiler.Jit.treeFrom(file.source)
    } yield Source(file, TreeParser.defDefExprsFrom(tree))
  }

  def main(args: Array[String]): Unit = {
    val functionTrees = loadFunctionTrees(Loader.testRoot)

    val kernels = for {
      ta ← functionTrees
      tb ← functionTrees
      _ = println(s"a ${ta.file.file} has ${ta.functions.size} function definitions")
      _ = println(s"b ${tb.file.file} has ${tb.functions.size} function definitions")
    } yield new kernel.ConvNLP(ta.functions.drop(1).head, tb.functions.drop(3).head)

    kernels.map(_.similarity).foreach(println)
  }
}
