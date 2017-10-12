package io.github.tdhd.ast.kernel.graph

import breeze.linalg.DenseVector

import scalax.collection.Graph
import scalax.collection.edge.LDiEdge

object VertexHistogram {
  def apply[T](first: Graph[T, LDiEdge], second: Graph[T, LDiEdge]): Double = {
    def count(graph: Graph[T, LDiEdge], bins: Seq[T]): Seq[Double] =
      bins.map { bin ⇒
        graph.nodes.count(_.value == bin).toDouble
      }

    val bins = (first.nodes.toSeq ++ second.nodes.toSeq).map(_.value).distinct

    val v1 = DenseVector(count(first, bins): _*)
    val v2 = DenseVector(count(second, bins): _*)

    v1.dot(v2) / (scala.math.sqrt(v1.dot(v1)) * scala.math.sqrt(v2.dot(v2)))
  }
}
