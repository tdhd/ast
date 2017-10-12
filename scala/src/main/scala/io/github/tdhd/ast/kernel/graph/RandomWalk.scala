package io.github.tdhd.ast.kernel.graph

import io.github.tdhd.ast.graph.GraphAug

import scalax.collection.Graph
import scalax.collection.edge.LDiEdge

object RandomWalk {

  def apply[T <: Ordered[T]](first: Graph[T, LDiEdge], second: Graph[T, LDiEdge], lambda: Double): Double = {
    import breeze.linalg.{DenseMatrix, inv}

    val pg = first.intersect(second)
//    println(
//      s"""
//         |A#nodes/#edges: ${first.nodes.size}/${first.edges.size}
//         |B#nodes/#edges: ${second.nodes.size}/${second.edges.size}
//         |P#nodes/#edges: ${pg.nodes.size}/${pg.edges.size}
//         |""".stripMargin)
    val A = pg.adjacencyMatrix
    val I = DenseMatrix.eye[Double](A.rows)

    inv(I - lambda * A).data.sum
  }


  // todo: RWK must compute adjacency matrices s.t. rows and cols match each other
  //      val A1 = graph.adjacencyMatrix
  //      val A2 = graph2.adjacencyMatrix
  //
  //      def rwk_(A1: DenseMatrix[Double], A2: DenseMatrix[Double], lambda: Double): Double = {
  //        import breeze.linalg.{ kron, inv, DenseMatrix }
  //        val k = kron(A1, A2)
  //        val I = DenseMatrix.eye[Double](k.rows)
  //        val i = inv(I - lambda * k)
  //        i.data.sum
  //      }
  //
  //      val r = rwk_(A1, A2, lambda = 0.1)
  //      val xx = 1
}
