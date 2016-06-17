package io.github.tdhd
package ast

import scala.meta._

object Main {

  def main(args: Array[String]): Unit = {

    val tests = io.Loader.sourceFilesFor(io.Loader.testRoot)
    val bodies = tests.head.functionBodies

    bodies.map(_.structure).foreach(println)
//    bodies.map(_.tokens).foreach(println)
    bodies.map(_.syntax).foreach(println)

    val sim = kernel.ConvNLP(bodies(2), bodies(3), lambda = 0.01)
    println(sim)
  }
}
