package io.github.tdhd.ast.kernel

// http://machinelearning.wustl.edu/mlpapers/paper_files/nips02-AA58.pdf
// Convolution Kernels for Natural Language

import scala.annotation.tailrec

trait Base {
  def similarity: Double
}

object ConvNLP {

  implicit class TreeAug(t: scala.reflect.internal.Trees#Tree) {
    def sameAs(other: scala.reflect.internal.Trees#Tree) = t.shortClass == other.shortClass

    def differentFrom(other: scala.reflect.internal.Trees#Tree) = !sameAs(other)
  }

  def bothTerminals(f: scala.reflect.internal.Trees#Tree, s: scala.reflect.internal.Trees#Tree) = f.children.isEmpty && s.children.isEmpty

  def allNodesOf(t: scala.reflect.internal.Trees#Tree): Seq[scala.reflect.internal.Trees#Tree] = {

    @tailrec
    def rec(elements: Seq[scala.reflect.internal.Trees#Tree], accumulator: Seq[scala.reflect.internal.Trees#Tree]): Seq[scala.reflect.internal.Trees#Tree] = elements match {
      case Nil ⇒ accumulator
      case elem :: tail ⇒ rec(elem.children ++ tail, elem +: accumulator)
    }

    rec(t :: Nil, Nil)
  }

}

class ConvNLP(val first: scala.reflect.internal.Trees#Tree, val second: scala.reflect.internal.Trees#Tree) extends Base {

  import ConvNLP._

  private def score(f: scala.reflect.internal.Trees#Tree, s: scala.reflect.internal.Trees#Tree): Double = {
    if (f differentFrom s)
      0.0
    else if ((f sameAs s) && bothTerminals(f, s))
      1.0
    else {

      val scores = for {
      // only applies to ordered trees
      //j ← 0 until scala.math.min(firstChildren.size, secondChildren.size)
        fc ← allNodesOf(f) diff Seq(f)
        sc ← allNodesOf(s) diff Seq(s)
      } yield 1 + score(fc, sc)

      scores.product
    }
  }

  def similarity: Double = {

    val scores = for {
      v1 ← allNodesOf(first)
      v2 ← allNodesOf(second)
    } yield score(v1, v2)

    scores.sum
  }
}
