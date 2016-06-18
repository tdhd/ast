package io.github.tdhd.ast.scoring

import io.github.tdhd.ast.kernel

import scala.meta.Defn.Def

object Scorer {

  def functionSimilaritiesFrom(defs: Seq[Def]): Map[(Def, Def), Double] = {
    val scores = for {
      first ← defs
      second ← defs
    } yield ((first, second), kernel.ConvNLP(first, second, 0.04))

    scores.toMap
  }
}
