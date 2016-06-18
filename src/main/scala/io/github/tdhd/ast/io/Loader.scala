package io.github.tdhd
package ast
package io

import java.io.File

import scala.meta._
import scala.util.matching.Regex

object Loader {
  val pattern = """.*\.scala$""".r
  val mainRoot = new File("src/main")
  val testRoot = new File("src/test/resources/code")

  case class LoaderSourceFile(
    file: File,
    source: String,
    functions: Seq[scala.meta.Defn.Def],
    vals: Seq[scala.meta.Defn.Val]
  )

  def loadFile(path: String): LoaderSourceFile = {
    val f = new File(path)
    val src = scala.io.Source.fromFile(f).mkString
    val functions = Parser.functionsOf(src)
    val vals = Parser.valsOf(src)
    LoaderSourceFile(f, src, functions, vals)
  }

  def sourceFilesFor(root: File): Seq[LoaderSourceFile] = {
    for {
      (file, src) ← Loader.allFiles(root)
      functions = Parser.functionsOf(src)
      vals = Parser.valsOf(src)
    } yield LoaderSourceFile(file, src, functions, vals)
  }

  private def allFiles(root: File): Seq[(File, String)] =
    filenames(root, pattern).map { file ⇒
      (file, scala.io.Source.fromFile(file).mkString)
    }

  private def filenames(f: File, r: Regex): Seq[File] = {
    val these = f.listFiles
    val good = these.filter(f ⇒ r.findFirstIn(f.getName).isDefined)
    good ++ these.filter(_.isDirectory).flatMap(filenames(_, r))
  }
}
