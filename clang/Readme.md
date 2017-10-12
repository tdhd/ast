https://eli.thegreenplace.net/2011/07/03/parsing-c-in-python-with-clang


	sudo apt-get install libclang-dev
	cd /usr/lib/x86_64-linux-gnu/
	sudo ln -s libclang-3.8.so.1 libclang.so
	pip install clang==3.8


Sample

```
In [1]: import clang.cindex

In [2]: index = clang.cindex.Index.create()

In [3]: tu = index.parse("main.c")

In [4]: q = []

In [5]: q.append(tu.cursor)

In [6]: while len(q) > 0:
   ...:     e = q[0]
   ...:     print e.location, e.kind, len(list(e.get_children()))
   ...:     for child in e.get_children():
   ...:         q.append(child)
   ...:     del q[0]
   ...:
<SourceLocation file None, line 0, column 0> CursorKind.TRANSLATION_UNIT 1
<SourceLocation file 'main.c', line 1, column 5> CursorKind.FUNCTION_DECL 1
<SourceLocation file 'main.c', line 1, column 16> CursorKind.COMPOUND_STMT 2
<SourceLocation file 'main.c', line 2, column 3> CursorKind.DECL_STMT 1
<SourceLocation file 'main.c', line 3, column 3> CursorKind.RETURN_STMT 1
<SourceLocation file 'main.c', line 2, column 7> CursorKind.VAR_DECL 1
<SourceLocation file 'main.c', line 3, column 10> CursorKind.BINARY_OPERATOR 2
<SourceLocation file 'main.c', line 2, column 11> CursorKind.INTEGER_LITERAL 0
<SourceLocation file 'main.c', line 3, column 10> CursorKind.BINARY_OPERATOR 2
<SourceLocation file 'main.c', line 3, column 27> CursorKind.INTEGER_LITERAL 0
<SourceLocation file 'main.c', line 3, column 10> CursorKind.BINARY_OPERATOR 2
<SourceLocation file 'main.c', line 3, column 24> CursorKind.INTEGER_LITERAL 0
<SourceLocation file 'main.c', line 3, column 10> CursorKind.BINARY_OPERATOR 2
<SourceLocation file 'main.c', line 3, column 21> CursorKind.INTEGER_LITERAL 0
<SourceLocation file 'main.c', line 3, column 10> CursorKind.BINARY_OPERATOR 2
<SourceLocation file 'main.c', line 3, column 18> CursorKind.INTEGER_LITERAL 0
<SourceLocation file 'main.c', line 3, column 10> CursorKind.BINARY_OPERATOR 2
<SourceLocation file 'main.c', line 3, column 15> CursorKind.INTEGER_LITERAL 0
<SourceLocation file 'main.c', line 3, column 10> CursorKind.INTEGER_LITERAL 0
<SourceLocation file 'main.c', line 3, column 12> CursorKind.INTEGER_LITERAL 0

In [7]:

```
