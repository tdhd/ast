package io.github.tdhd
package ast

object Main {

  def main(args: Array[String]): Unit = {

    val f = io.Loader.loadFile("src/test/resources/code/Test.scala")

//    import scala.pickling.Defaults._, scala.pickling.json._
//    val pickled = f.functions.head.pickle
//    println(pickled)

    val fnRoot = f.functions.drop(8).head
    import org.json4s.jackson.Serialization.write
    val json = write(translation.translate(fnRoot) :: Nil)
    scala.tools.nsc.io.File("src/main/resources/treeData.json").writeAll(json)

//    val scores = Scorer.functionSimilaritiesFrom(f.functions)
//    scores.map {
//      case ((a, b), s) ⇒ (a.name, b.name, s)
//    }.toSeq.sortBy(-_._3).foreach(println)
  }
}
