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

  case class LoadedFile(file: File, source: String)
  case class LoaderSourceFile(file: LoadedFile, parsedSource: Parsed[scala.meta.Source], functionBodies: Seq[Tree])

  def sourceFilesFor(root: File): Seq[LoaderSourceFile] = {
    for {
      file ← Loader.allFiles(root)
      fileSource = file.source.parse[Source]
      functionBodies = fileSource.get.collect {
        case q"..$mods def $tname[..$tparams](...$paramss): $tpe = $ex" ⇒
          ex
      }
    } yield LoaderSourceFile(file, fileSource, functionBodies)
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
