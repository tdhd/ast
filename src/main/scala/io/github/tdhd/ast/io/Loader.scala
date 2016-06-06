package io.github.tdhd.ast.io

import java.io.File

import scala.util.matching.Regex

object Loader {
  val pattern = """.*\.scala$""".r
  val root = new File(".")

  val allFiles = filenames(root, pattern).map { file ⇒
    scala.io.Source.fromFile(file).mkString.replaceAll("package", "//package")
  }

  def filenames(f: File, r: Regex): Seq[File] = {
    val these = f.listFiles
    val good = these.filter(f ⇒ r.findFirstIn(f.getName).isDefined)
    good ++ these.filter(_.isDirectory).flatMap(filenames(_, r))
  }
}
