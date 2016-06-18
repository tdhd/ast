package io.github.tdhd
package ast
package kernel

// http://machinelearning.wustl.edu/mlpapers/paper_files/nips02-AA58.pdf
// Convolution Kernels for Natural Language

import scala.annotation.tailrec
import scala.meta.Tree

object ConvNLP {
  def apply(first: Tree, second: Tree, lambda: Double) = {
    val normalizer =
      math.sqrt(new ConvNLP(first, first, lambda).similarity * new ConvNLP(second, second, lambda).similarity)

    new ConvNLP(first, second, lambda).similarity / normalizer
  }

  def allNodesOf(t: Tree): Seq[Tree] = {
    @tailrec
    def rec(elements: Seq[Tree], accumulator: Seq[Tree]): Seq[Tree] = elements match {
      case Nil ⇒ accumulator
      case elem :: tail ⇒ rec(elem.children ++ tail, elem +: accumulator)
    }

    rec(t :: Nil, Nil)
  }

  def bothTerminals(f: Tree, s: Tree) = f.children.isEmpty && s.children.isEmpty
}

class ConvNLP(val first: Tree, val second: Tree, val lambda: Double) extends Base {

  import ConvNLP._

  private def score(f: Tree, s: Tree): Double = {

    (f, s) match {
      case (ff, ss) if ff.getClass != ss.getClass ⇒ 0.0
      case (ff, ss) if ff.getClass == ss.getClass && bothTerminals(ff, ss) ⇒ lambda
      case (ff, ss) ⇒
        val scores = for {
        // only applies to ordered trees
        //j ← 0 until scala.math.min(firstChildren.size, secondChildren.size)
          fc ← allNodesOf(f) diff Seq(f)
          sc ← allNodesOf(s) diff Seq(s)
        } yield 1 + score(fc, sc)

        lambda * scores.product
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
