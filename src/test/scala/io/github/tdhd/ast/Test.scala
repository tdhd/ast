package io.github.tdhd.ast

class Test {

  def iSeq(p: Seq[Int]) = {
    p.map(_ * 2).filter(_ % 2 == 0).take(2)
  }
  def sSeq(p: Seq[String]) = {
    p.filter(_.startsWith("a")).take(2)
  }
  def qSeq(p: Seq[Double]) = p.head
}
