# AST - `Abstract Syntax Tree`

This project is a small demo of AST parsing with the new `scala.meta`. On top of the parsed data structures,
which are trees, I've implemented several graph kernels.

One part of the project is concerned with parsing scala source files into `scala.meta.Tree`, another one
takes care of translating the ASTs into generic graph structures.
Once the AST has been transformed into a generic graph, several graph kernels can be used to measure similarity
between pairs of graphs.
