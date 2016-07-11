package io.github.tdhd
package ast
package tree

object Metrics {

  def size(tree: scala.meta.Tree): Int = {

    def s(tree: scala.meta.Tree): Int = tree.children match {
      case h :: t ⇒ 1 + t.map(s).sum
      case Nil ⇒ 0
    }

    1 + s(tree)

  }

  def depth(tree: scala.meta.Tree): Int = {
    def inner(tree: scala.meta.Tree, currentDepth: Int): Int = tree.children match {
      case Nil ⇒ currentDepth
      case h :: Nil ⇒ currentDepth
      case h :: t ⇒ t.map(inner(_, currentDepth + 1)).max
    }

    inner(tree, currentDepth = 0)
  }

}
