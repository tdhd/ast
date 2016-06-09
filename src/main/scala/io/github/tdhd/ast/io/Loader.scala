package io.github.tdhd
package ast
package io

import java.io.File

import parser.TreeParser.DefDef

import scala.util.matching.Regex

object Loader {
  val pattern = """.*\.scala$""".r
  val mainRoot = new File("src/main")
  val testRoot = new File("src/test/resources/code")

  case class LoadedFile(file: File, source: String)
  case class Source(file: LoadedFile, functions: Seq[DefDef])

  def functionBodiesFor(root: File): Seq[Source] = {
    for {
      file ← Loader.allFiles(root)
      fileSourceTree = compiler.Jit.treeFrom(file.source)
    } yield Source(file, parser.TreeParser.defdefFrom(fileSourceTree))
  }

  private def allFiles(root: File): Seq[LoadedFile] =
    filenames(root, pattern).map { file ⇒
      LoadedFile(file, scala.io.Source.fromFile(file).mkString.replaceAll("package", "//package"))
    }

  private def filenames(f: File, r: Regex): Seq[File] = {
    val these = f.listFiles
    val good = these.filter(f ⇒ r.findFirstIn(f.getName).isDefined)
    good ++ these.filter(_.isDirectory).flatMap(filenames(_, r))
  }
}
