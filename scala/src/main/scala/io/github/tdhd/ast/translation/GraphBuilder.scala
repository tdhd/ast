package io.github.tdhd
package ast
package translation

import scala.meta.Tree

object GraphBuilder {

  case class Node(id: String, label: String)
  case class Edge(id: String, from: String, to: String)
  case class Graph(nodes: Seq[Node], edges: Seq[Edge])

  private def nextId = java.util.UUID.randomUUID().toString

  case class TranslatedNode(id: String, name: String, parent: Option[String], children: Seq[TranslatedNode])
  val name: Tree ⇒ String = t ⇒ t.syntax

  def apply(tree: Tree): (Seq[Node], Seq[Edge]) = {
    val builder = new GraphBuilder(tree)
    (builder.nodes, builder.edges)
  }
}

class GraphBuilder(tree: Tree) {

  import GraphBuilder._

  private var nodes = Seq.empty[Node]
  private var edges = Seq.empty[Edge]

  private def graphDefFrom(current: TranslatedNode): Unit = {
    nodes +:= Node(current.id, current.name)
    current.children.foreach { child ⇒
      edges +:= Edge(nextId, current.id, child.id)
    }
    current.children.foreach(graphDefFrom)
  }

  private def translate(current: Tree): TranslatedNode =
    TranslatedNode(nextId, name(current), current.parent.map(name), current.children.map(translate))

  private val buildGraph = translate _ andThen graphDefFrom

  buildGraph(tree)

}
