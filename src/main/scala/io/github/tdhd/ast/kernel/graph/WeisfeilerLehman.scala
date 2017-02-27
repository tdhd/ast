package io.github.tdhd.ast.kernel.graph

import scalax.collection.Graph
import scalax.collection.GraphEdge.UnDiEdge
import scalax.collection.edge.{LDiEdge, LUnDiEdge}

object WeisfeilerLehman {

  object Isomorphism {
    // test the two graphs for isomorphism
    def apply(first: Graph[String, UnDiEdge], second: Graph[String, UnDiEdge]): Boolean = {

      def connectionsFor(node: String, graph: Graph[String, UnDiEdge]): Set[String] = {
        val n = graph.edges.filter(_._1 == node).map(_._2) ++
          graph.edges.filter(_._2 == node).map(_._1)
        n.map(_.value).toSet
      }

      val edgesFirst: scala.collection.mutable.Map[String, Set[String]] = scala.collection.mutable.Map(
        first.nodes.map(n ⇒ n → n.diPredecessors).map { case (node, connections) ⇒
          node.value → connections.map(_.value)
        }.toSeq:_*
      )
      val edgesSecond: scala.collection.mutable.Map[String, Set[String]] = scala.collection.mutable.Map(
        second.nodes.map(n ⇒ n → n.diPredecessors).map { case (node, connections) ⇒
          node.value → connections.map(_.value)
        }.toSeq:_*
      )
      // mapping from old to new nodes
      var transFirst: scala.collection.mutable.Map[String, String] = scala.collection.mutable.Map(
        first.nodes.map(_.value).map(n ⇒ n → n).toSeq: _*
      )
      var transSecond: scala.collection.mutable.Map[String, String] = scala.collection.mutable.Map(
        second.nodes.map(_.value).map(n ⇒ n → n).toSeq: _*
      )

      val tempFirst: scala.collection.mutable.Map[String, String] = scala.collection.mutable.Map.empty
      val tempSecond: scala.collection.mutable.Map[String, String] = scala.collection.mutable.Map.empty


      for(i ← 1 to 10) {
        // step 1 and 2: label assignment
        // step 3: label compression
        // step 4
        // isomorphic?
      }
      ???
    }
  }

  case class WLVertex[T](data: T, label: String)
  def apply[T <: WLVertex[T]](first: Graph[T, LDiEdge], second: Graph[T, LDiEdge], lambda: Double): Double = {
    ???
  }
}
