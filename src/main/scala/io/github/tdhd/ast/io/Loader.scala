package io.github.tdhd.ast.io

import java.io.File

import scala.util.matching.Regex

object Loader {
  val pattern = """.*\.scala$""".r
  val mainRoot = new File("src/main")
  val testRoot = new File("src/test")

  println(mainRoot.getAbsoluteFile)
  println(testRoot.getAbsoluteFile)

  val allFiles: File ⇒ Seq[(String, String)] = root ⇒ filenames(root, pattern).map { file ⇒
    (file.getName, scala.io.Source.fromFile(file).mkString.replaceAll("package", "//package"))
  }

  def filenames(f: File, r: Regex): Seq[File] = {
    val these = f.listFiles
    val good = these.filter(f ⇒ r.findFirstIn(f.getName).isDefined)
    good ++ these.filter(_.isDirectory).flatMap(filenames(_, r))
  }

  def testMethod(f: File) = {
    val these = f.listFiles
    these.filter(x ⇒ x.canRead)
    val g = 1 :: Nil
    (g ++ g).flatMap(_ ⇒ 1 :: Nil)
    "1" :: "2" :: Nil map {
      _ * 2
    }
  }
}
