package io.github.tdhd.ast.kernel.graph

import org.scalatest.{Inspectors, Matchers, WordSpec}

import scalax.collection.Graph
import scalax.collection.edge.Implicits._
import scalax.collection.GraphPredef._

class WeisfeilerLehmanTest extends WordSpec with Matchers with Inspectors {

  "WL test of isomorphism" should {
    case class V(i: Int) extends Ordered[V] {
      override def compare(that: V): Int = i.compareTo(that.i)
    }
    "detect structurally different graphs" in {
      pending
//      val first = Graph(
//        V(1) ~ V(4),
//        V(4) ~ V(2), V(4) ~ V(3), V(4) ~ V(5),
//        V(3) ~ V(5),
//        V(2) ~ V(5)
//      )
//
//      val second = Graph(
//        V(1) ~ V(4),
//        V(4) ~ V(2), V(4) ~ V(3),
//        V(3) ~ V(2),
//        V(3) ~ V(5),
//        V(2) ~ V(5)
//      )
      val first = Graph(
        "1" ~ "4",
        "4" ~ "2", "4" ~ "3", "4" ~ "5",
        "3" ~ "5",
        "2" ~ "5"
      )
      val second = Graph(
        "1" ~ "4",
        "4" ~ "2", "4" ~ "3",
        "3" ~ "2",
        "3" ~ "5",
        "2" ~ "5"
      )

      WeisfeilerLehman.Isomorphism(first, second) shouldBe false
    }
  }

  "WL kernel" should {
    "yield 0 for completely different graphs" in {
      pending
    }
  }
}
