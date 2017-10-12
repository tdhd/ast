package io.github.tdhd.ast

import breeze.linalg.DenseMatrix

import scalax.collection.Graph
import scalax.collection.edge.LDiEdge

package object graph {

  case class TypeNode(name: String) extends Ordered[TypeNode] {
    override def compare(that: TypeNode): Int = name.compareTo(that.name)
  }

  def directedScalaMetaTypeGraphFrom(tree: scala.meta.Tree): Graph[TypeNode, LDiEdge] = {
    import scalax.collection.edge.Implicits._
    def r(tree: scala.meta.Tree): Seq[LDiEdge[TypeNode]] = {
      val edges = tree.children.map { child ⇒
        val label =
          if (child.syntax.nonEmpty) s"synt: ${child.syntax}"
          else s"struct: ${child.structure}"
        (TypeNode(tree.getClass.toString) ~+> TypeNode(child.getClass.toString)) (label)
      }
      edges ++ tree.children.flatMap(r)
    }

    val nodes = r(tree)
    Graph(nodes: _*)
  }

  //  def directedLabeledGraphFrom(tree: scala.meta.Tree): Graph[scala.meta.Tree, LDiEdge] = {
  //    import scalax.collection.GraphPredef._
  //    import scalax.collection.edge.Implicits._
  //    def r(tree: scala.meta.Tree): Seq[LDiEdge[scala.meta.Tree]] = {
  //      val edges = tree.children.map { child ⇒
  //        (tree ~+> child)(child.getClass)
  //      }
  //      edges ++ tree.children.flatMap(r)
  //    }
  //    val nodes = r(tree)
  //    Graph(nodes:_*)
  //  }

  implicit class GraphAug[T <: Ordered[T]](underlying: Graph[T, LDiEdge]) {

    def toDotFormat: String = {
      import scalax.collection.Graph
      import scalax.collection.edge.LDiEdge
      import scalax.collection.io.dot._
      import implicits._

      val root = DotRootGraph(directed = true, id = Some("test"))

      def edgeTransformer(innerEdge: Graph[T, LDiEdge]#EdgeT): Option[(DotGraph, DotEdgeStmt)] =
        innerEdge.edge match {
          case LDiEdge(source, target, label) ⇒
            Some((root,
              DotEdgeStmt(
                if (source.toString == "") "empty-source" else source.toString,
                if (target.toString == "") "empty-target" else target.toString,
                if (label.toString.nonEmpty) List(DotAttr("label", label.toString.replace(""""""", "")))
                else Nil)))
        }

      underlying.toDot(root, edgeTransformer)
    }

    def adjacencyMatrix: DenseMatrix[Double] = {
      val nodes = underlying.nodes.toSeq.sortBy(_.value)
      val n = nodes.size

      val edges = underlying.edges.map { e ⇒
        e._1.value → e._2.value
      }

      val data = nodes.flatMap { i ⇒
        nodes.map { j ⇒
          val a = edges.find { edge ⇒
            edge._1 == j.value &&
              edge._2 == i.value
          }.map(_ ⇒ 1.0).getOrElse(0.0)

          a
        }
      }
      new DenseMatrix(rows = n, cols = n, data = data.toArray)
    }
  }

}
