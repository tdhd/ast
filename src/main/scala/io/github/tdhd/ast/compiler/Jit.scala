package io.github.tdhd.ast.compiler

import scala.reflect.runtime.{currentMirror, universe}
import scala.tools.reflect.ToolBox

object Jit {

  val toolbox = currentMirror.mkToolBox()

  def treeFrom(src: String): universe.Tree = toolbox.parse(src)
}
