package io.github.tdhd.ast.compiler

object Jit {

  val universe = scala.reflect.runtime.universe

  import universe._
  import scala.reflect.runtime.currentMirror
  import scala.tools.reflect.ToolBox

  val toolbox = currentMirror.mkToolBox()

  def treeFrom(src: String): universe.Tree =
    toolbox.parse(src).asInstanceOf[universe.Tree]
}
