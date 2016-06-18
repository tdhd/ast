package io.github.tdhd.ast

import scala.meta.Tree

package object translation {
  case class Node(name: String, parent: Option[String], children: Seq[Node])

  val name: Tree ⇒ String = t ⇒ t.syntax

  def translate(current: Tree): Node =
    Node(name(current), current.parent.map(name), current.children.map(translate))
}
