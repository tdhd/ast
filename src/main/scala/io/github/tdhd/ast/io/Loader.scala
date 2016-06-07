package io.github.tdhd.ast.io

import java.io.File

import scala.util.matching.Regex

object Loader {
  val pattern = """.*\.scala$""".r
  val mainRoot = new File("src/main")
  val testRoot = new File("src/test")

  case class LoadedFile(file: File, source: String)

  val allFiles: File ⇒ Seq[LoadedFile] = root ⇒ filenames(root, pattern).map { file ⇒
    LoadedFile(file, scala.io.Source.fromFile(file).mkString.replaceAll("package", "//package"))
  }

  def filenames(f: File, r: Regex): Seq[File] = {
    val these = f.listFiles
    val good = these.filter(f ⇒ r.findFirstIn(f.getName).isDefined)
    good ++ these.filter(_.isDirectory).flatMap(filenames(_, r))
  }
}
