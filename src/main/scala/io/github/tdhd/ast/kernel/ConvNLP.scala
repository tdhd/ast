package io.github.tdhd.ast.kernel

// http://machinelearning.wustl.edu/mlpapers/paper_files/nips02-AA58.pdf
// Convolution Kernels for Natural Language

import scala.reflect.internal.Trees

import scala.annotation.tailrec

// todo: need normalization of scores

trait Base {
  def similarity: Double
}

object ConvNLP {

  implicit class TreeAug(t: Trees#Tree) {
    def sameAs(other: Trees#Tree) = t.shortClass == other.shortClass

    def differentFrom(other: Trees#Tree) = !sameAs(other)
  }

  def bothTerminals(f: Trees#Tree, s: Trees#Tree) = f.children.isEmpty && s.children.isEmpty

  def allNodesOf(t: Trees#Tree): Seq[Trees#Tree] = {

    @tailrec
    def rec(elements: Seq[Trees#Tree], accumulator: Seq[Trees#Tree]): Seq[Trees#Tree] = elements match {
      case Nil ⇒ accumulator
      case elem :: tail ⇒ rec(elem.children ++ tail, elem +: accumulator)
    }

    rec(t :: Nil, Nil)
  }

}

class ConvNLP(val first: Trees#Tree, val second: Trees#Tree) extends Base {

  import ConvNLP._

  private def score(f: Trees#Tree, s: Trees#Tree): Double = {
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
