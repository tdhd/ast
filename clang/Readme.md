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

```
In [28]: import numpy as np

In [29]: np.array(clang.cindex.CursorKind._kinds)
Out[29]:
array([None, CursorKind.UNEXPOSED_DECL, CursorKind.STRUCT_DECL,
       CursorKind.UNION_DECL, CursorKind.CLASS_DECL, CursorKind.ENUM_DECL,
       CursorKind.FIELD_DECL, CursorKind.ENUM_CONSTANT_DECL,
       CursorKind.FUNCTION_DECL, CursorKind.VAR_DECL, CursorKind.PARM_DECL,
       CursorKind.OBJC_INTERFACE_DECL, CursorKind.OBJC_CATEGORY_DECL,
       CursorKind.OBJC_PROTOCOL_DECL, CursorKind.OBJC_PROPERTY_DECL,
       CursorKind.OBJC_IVAR_DECL, CursorKind.OBJC_INSTANCE_METHOD_DECL,
       CursorKind.OBJC_CLASS_METHOD_DECL,
       CursorKind.OBJC_IMPLEMENTATION_DECL,
       CursorKind.OBJC_CATEGORY_IMPL_DECL, CursorKind.TYPEDEF_DECL,
       CursorKind.CXX_METHOD, CursorKind.NAMESPACE,
       CursorKind.LINKAGE_SPEC, CursorKind.CONSTRUCTOR,
       CursorKind.DESTRUCTOR, CursorKind.CONVERSION_FUNCTION,
       CursorKind.TEMPLATE_TYPE_PARAMETER,
       CursorKind.TEMPLATE_NON_TYPE_PARAMETER,
       CursorKind.TEMPLATE_TEMPLATE_PARAMETER,
       CursorKind.FUNCTION_TEMPLATE, CursorKind.CLASS_TEMPLATE,
       CursorKind.CLASS_TEMPLATE_PARTIAL_SPECIALIZATION,
       CursorKind.NAMESPACE_ALIAS, CursorKind.USING_DIRECTIVE,
       CursorKind.USING_DECLARATION, CursorKind.TYPE_ALIAS_DECL,
       CursorKind.OBJC_SYNTHESIZE_DECL, CursorKind.OBJC_DYNAMIC_DECL,
       CursorKind.CXX_ACCESS_SPEC_DECL, CursorKind.OBJC_SUPER_CLASS_REF,
       CursorKind.OBJC_PROTOCOL_REF, CursorKind.OBJC_CLASS_REF,
       CursorKind.TYPE_REF, CursorKind.CXX_BASE_SPECIFIER,
       CursorKind.TEMPLATE_REF, CursorKind.NAMESPACE_REF,
       CursorKind.MEMBER_REF, CursorKind.LABEL_REF,
       CursorKind.OVERLOADED_DECL_REF, CursorKind.VARIABLE_REF, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, CursorKind.INVALID_FILE,
       CursorKind.NO_DECL_FOUND, CursorKind.NOT_IMPLEMENTED,
       CursorKind.INVALID_CODE, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None,
       CursorKind.UNEXPOSED_EXPR, CursorKind.DECL_REF_EXPR,
       CursorKind.MEMBER_REF_EXPR, CursorKind.CALL_EXPR,
       CursorKind.OBJC_MESSAGE_EXPR, CursorKind.BLOCK_EXPR,
       CursorKind.INTEGER_LITERAL, CursorKind.FLOATING_LITERAL,
       CursorKind.IMAGINARY_LITERAL, CursorKind.STRING_LITERAL,
       CursorKind.CHARACTER_LITERAL, CursorKind.PAREN_EXPR,
       CursorKind.UNARY_OPERATOR, CursorKind.ARRAY_SUBSCRIPT_EXPR,
       CursorKind.BINARY_OPERATOR, CursorKind.COMPOUND_ASSIGNMENT_OPERATOR,
       CursorKind.CONDITIONAL_OPERATOR, CursorKind.CSTYLE_CAST_EXPR,
       CursorKind.COMPOUND_LITERAL_EXPR, CursorKind.INIT_LIST_EXPR,
       CursorKind.ADDR_LABEL_EXPR, CursorKind.StmtExpr,
       CursorKind.GENERIC_SELECTION_EXPR, CursorKind.GNU_NULL_EXPR,
       CursorKind.CXX_STATIC_CAST_EXPR, CursorKind.CXX_DYNAMIC_CAST_EXPR,
       CursorKind.CXX_REINTERPRET_CAST_EXPR,
       CursorKind.CXX_CONST_CAST_EXPR, CursorKind.CXX_FUNCTIONAL_CAST_EXPR,
       CursorKind.CXX_TYPEID_EXPR, CursorKind.CXX_BOOL_LITERAL_EXPR,
       CursorKind.CXX_NULL_PTR_LITERAL_EXPR, CursorKind.CXX_THIS_EXPR,
       CursorKind.CXX_THROW_EXPR, CursorKind.CXX_NEW_EXPR,
       CursorKind.CXX_DELETE_EXPR, CursorKind.CXX_UNARY_EXPR,
       CursorKind.OBJC_STRING_LITERAL, CursorKind.OBJC_ENCODE_EXPR,
       CursorKind.OBJC_SELECTOR_EXPR, CursorKind.OBJC_PROTOCOL_EXPR,
       CursorKind.OBJC_BRIDGE_CAST_EXPR, CursorKind.PACK_EXPANSION_EXPR,
       CursorKind.SIZE_OF_PACK_EXPR, CursorKind.LAMBDA_EXPR,
       CursorKind.OBJ_BOOL_LITERAL_EXPR, CursorKind.OBJ_SELF_EXPR, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None,
       CursorKind.UNEXPOSED_STMT, CursorKind.LABEL_STMT,
       CursorKind.COMPOUND_STMT, CursorKind.CASE_STMT,
       CursorKind.DEFAULT_STMT, CursorKind.IF_STMT, CursorKind.SWITCH_STMT,
       CursorKind.WHILE_STMT, CursorKind.DO_STMT, CursorKind.FOR_STMT,
       CursorKind.GOTO_STMT, CursorKind.INDIRECT_GOTO_STMT,
       CursorKind.CONTINUE_STMT, CursorKind.BREAK_STMT,
       CursorKind.RETURN_STMT, CursorKind.ASM_STMT,
       CursorKind.OBJC_AT_TRY_STMT, CursorKind.OBJC_AT_CATCH_STMT,
       CursorKind.OBJC_AT_FINALLY_STMT, CursorKind.OBJC_AT_THROW_STMT,
       CursorKind.OBJC_AT_SYNCHRONIZED_STMT,
       CursorKind.OBJC_AUTORELEASE_POOL_STMT,
       CursorKind.OBJC_FOR_COLLECTION_STMT, CursorKind.CXX_CATCH_STMT,
       CursorKind.CXX_TRY_STMT, CursorKind.CXX_FOR_RANGE_STMT,
       CursorKind.SEH_TRY_STMT, CursorKind.SEH_EXCEPT_STMT,
       CursorKind.SEH_FINALLY_STMT, CursorKind.MS_ASM_STMT,
       CursorKind.NULL_STMT, CursorKind.DECL_STMT, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None,
       CursorKind.TRANSLATION_UNIT, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, CursorKind.UNEXPOSED_ATTR,
       CursorKind.IB_ACTION_ATTR, CursorKind.IB_OUTLET_ATTR,
       CursorKind.IB_OUTLET_COLLECTION_ATTR, CursorKind.CXX_FINAL_ATTR,
       CursorKind.CXX_OVERRIDE_ATTR, CursorKind.ANNOTATE_ATTR,
       CursorKind.ASM_LABEL_ATTR, CursorKind.PACKED_ATTR,
       CursorKind.PURE_ATTR, CursorKind.CONST_ATTR,
       CursorKind.NODUPLICATE_ATTR, CursorKind.CUDACONSTANT_ATTR,
       CursorKind.CUDADEVICE_ATTR, CursorKind.CUDAGLOBAL_ATTR,
       CursorKind.CUDAHOST_ATTR, CursorKind.CUDASHARED_ATTR,
       CursorKind.VISIBILITY_ATTR, CursorKind.DLLEXPORT_ATTR,
       CursorKind.DLLIMPORT_ATTR, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None,
       CursorKind.PREPROCESSING_DIRECTIVE, CursorKind.MACRO_DEFINITION,
       CursorKind.MACRO_INSTANTIATION, CursorKind.INCLUSION_DIRECTIVE,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None, None, None, None,
       None, None, None, None, None, None, None, None,
       CursorKind.MODULE_IMPORT_DECL, CursorKind.TYPE_ALIAS_TEMPLATE_DECL], dtype=object)

In [30]: np.argwhere(np.array(clang.cindex.CursorKind._kinds) != None)
Out[30]:
array([[  1],
       [  2],
       [  3],
       [  4],
       [  5],
       [  6],
       [419],
       [500],
       [501],
       [502],
       [503],
       [600],
       [601]])

In [31]: idcs = np.argwhere(np.array(clang.cindex.CursorKind._kinds) != None)

In [32]: kinds = [e for e in clang.cindex.CursorKind._kinds if e is not None]

In [33]: kinds
Out[33]:
[CursorKind.UNEXPOSED_DECL,
 CursorKind.STRUCT_DECL,
 CursorKind.UNION_DECL,
 CursorKind.CLASS_DECL,
 CursorKind.ENUM_DECL,
 CursorKind.FIELD_DECL,
 CursorKind.ENUM_CONSTANT_DECL,
 CursorKind.FUNCTION_DECL,
 CursorKind.VAR_DECL,
 CursorKind.PARM_DECL,
 CursorKind.OBJC_INTERFACE_DECL,
 CursorKind.OBJC_CATEGORY_DECL,
 CursorKind.OBJC_PROTOCOL_DECL,
 CursorKind.OBJC_PROPERTY_DECL,
 CursorKind.OBJC_IVAR_DECL,
 CursorKind.OBJC_INSTANCE_METHOD_DECL,
 CursorKind.OBJC_CLASS_METHOD_DECL,
 CursorKind.OBJC_IMPLEMENTATION_DECL,
 CursorKind.OBJC_CATEGORY_IMPL_DECL,
 CursorKind.TYPEDEF_DECL,
 CursorKind.CXX_METHOD,
 CursorKind.NAMESPACE,
 CursorKind.LINKAGE_SPEC,
 CursorKind.CONSTRUCTOR,
 CursorKind.DESTRUCTOR,
 CursorKind.CONVERSION_FUNCTION,
 CursorKind.TEMPLATE_TYPE_PARAMETER,
 CursorKind.TEMPLATE_NON_TYPE_PARAMETER,
 CursorKind.TEMPLATE_TEMPLATE_PARAMETER,
 CursorKind.FUNCTION_TEMPLATE,
 CursorKind.CLASS_TEMPLATE,
 CursorKind.CLASS_TEMPLATE_PARTIAL_SPECIALIZATION,
 CursorKind.NAMESPACE_ALIAS,
 CursorKind.USING_DIRECTIVE,
 CursorKind.USING_DECLARATION,
 CursorKind.TYPE_ALIAS_DECL,
 CursorKind.OBJC_SYNTHESIZE_DECL,
 CursorKind.OBJC_DYNAMIC_DECL,
 CursorKind.CXX_ACCESS_SPEC_DECL,
 CursorKind.OBJC_SUPER_CLASS_REF,
 CursorKind.OBJC_PROTOCOL_REF,
 CursorKind.OBJC_CLASS_REF,
 CursorKind.TYPE_REF,
 CursorKind.CXX_BASE_SPECIFIER,
 CursorKind.TEMPLATE_REF,
 CursorKind.NAMESPACE_REF,
 CursorKind.MEMBER_REF,
 CursorKind.LABEL_REF,
 CursorKind.OVERLOADED_DECL_REF,
 CursorKind.VARIABLE_REF,
 CursorKind.INVALID_FILE,
 CursorKind.NO_DECL_FOUND,
 CursorKind.NOT_IMPLEMENTED,
 CursorKind.INVALID_CODE,
 CursorKind.UNEXPOSED_EXPR,
 CursorKind.DECL_REF_EXPR,
 CursorKind.MEMBER_REF_EXPR,
 CursorKind.CALL_EXPR,
 CursorKind.OBJC_MESSAGE_EXPR,
 CursorKind.BLOCK_EXPR,
 CursorKind.INTEGER_LITERAL,
 CursorKind.FLOATING_LITERAL,
 CursorKind.IMAGINARY_LITERAL,
 CursorKind.STRING_LITERAL,
 CursorKind.CHARACTER_LITERAL,
 CursorKind.PAREN_EXPR,
 CursorKind.UNARY_OPERATOR,
 CursorKind.ARRAY_SUBSCRIPT_EXPR,
 CursorKind.BINARY_OPERATOR,
 CursorKind.COMPOUND_ASSIGNMENT_OPERATOR,
 CursorKind.CONDITIONAL_OPERATOR,
 CursorKind.CSTYLE_CAST_EXPR,
 CursorKind.COMPOUND_LITERAL_EXPR,
 CursorKind.INIT_LIST_EXPR,
 CursorKind.ADDR_LABEL_EXPR,
 CursorKind.StmtExpr,
 CursorKind.GENERIC_SELECTION_EXPR,
 CursorKind.GNU_NULL_EXPR,
 CursorKind.CXX_STATIC_CAST_EXPR,
 CursorKind.CXX_DYNAMIC_CAST_EXPR,
 CursorKind.CXX_REINTERPRET_CAST_EXPR,
 CursorKind.CXX_CONST_CAST_EXPR,
 CursorKind.CXX_FUNCTIONAL_CAST_EXPR,
 CursorKind.CXX_TYPEID_EXPR,
 CursorKind.CXX_BOOL_LITERAL_EXPR,
 CursorKind.CXX_NULL_PTR_LITERAL_EXPR,
 CursorKind.CXX_THIS_EXPR,
 CursorKind.CXX_THROW_EXPR,
 CursorKind.CXX_NEW_EXPR,
 CursorKind.CXX_DELETE_EXPR,
 CursorKind.CXX_UNARY_EXPR,
 CursorKind.OBJC_STRING_LITERAL,
 CursorKind.OBJC_ENCODE_EXPR,
 CursorKind.OBJC_SELECTOR_EXPR,
 CursorKind.OBJC_PROTOCOL_EXPR,
 CursorKind.OBJC_BRIDGE_CAST_EXPR,
 CursorKind.PACK_EXPANSION_EXPR,
 CursorKind.SIZE_OF_PACK_EXPR,
 CursorKind.LAMBDA_EXPR,
 CursorKind.OBJ_BOOL_LITERAL_EXPR,
 CursorKind.OBJ_SELF_EXPR,
 CursorKind.UNEXPOSED_STMT,
 CursorKind.LABEL_STMT,
 CursorKind.COMPOUND_STMT,
 CursorKind.CASE_STMT,
 CursorKind.DEFAULT_STMT,
 CursorKind.IF_STMT,
 CursorKind.SWITCH_STMT,
 CursorKind.WHILE_STMT,
 CursorKind.DO_STMT,
 CursorKind.FOR_STMT,
 CursorKind.GOTO_STMT,
 CursorKind.INDIRECT_GOTO_STMT,
 CursorKind.CONTINUE_STMT,
 CursorKind.BREAK_STMT,
 CursorKind.RETURN_STMT,
 CursorKind.ASM_STMT,
 CursorKind.OBJC_AT_TRY_STMT,
 CursorKind.OBJC_AT_CATCH_STMT,
 CursorKind.OBJC_AT_FINALLY_STMT,
 CursorKind.OBJC_AT_THROW_STMT,
 CursorKind.OBJC_AT_SYNCHRONIZED_STMT,
 CursorKind.OBJC_AUTORELEASE_POOL_STMT,
 CursorKind.OBJC_FOR_COLLECTION_STMT,
 CursorKind.CXX_CATCH_STMT,
 CursorKind.CXX_TRY_STMT,
 CursorKind.CXX_FOR_RANGE_STMT,
 CursorKind.SEH_TRY_STMT,
 CursorKind.SEH_EXCEPT_STMT,
 CursorKind.SEH_FINALLY_STMT,
 CursorKind.MS_ASM_STMT,
 CursorKind.NULL_STMT,
 CursorKind.DECL_STMT,
 CursorKind.TRANSLATION_UNIT,
 CursorKind.UNEXPOSED_ATTR,
 CursorKind.IB_ACTION_ATTR,
 CursorKind.IB_OUTLET_ATTR,
 CursorKind.IB_OUTLET_COLLECTION_ATTR,
 CursorKind.CXX_FINAL_ATTR,
 CursorKind.CXX_OVERRIDE_ATTR,
 CursorKind.ANNOTATE_ATTR,
 CursorKind.ASM_LABEL_ATTR,
 CursorKind.PACKED_ATTR,
 CursorKind.PURE_ATTR,
 CursorKind.CONST_ATTR,
 CursorKind.NODUPLICATE_ATTR,
 CursorKind.CUDACONSTANT_ATTR,
 CursorKind.CUDADEVICE_ATTR,
 CursorKind.CUDAGLOBAL_ATTR,
 CursorKind.CUDAHOST_ATTR,
 CursorKind.CUDASHARED_ATTR,
 CursorKind.VISIBILITY_ATTR,
 CursorKind.DLLEXPORT_ATTR,
 CursorKind.DLLIMPORT_ATTR,
 CursorKind.PREPROCESSING_DIRECTIVE,
 CursorKind.MACRO_DEFINITION,
 CursorKind.MACRO_INSTANTIATION,
 CursorKind.INCLUSION_DIRECTIVE,
 CursorKind.MODULE_IMPORT_DECL,
 CursorKind.TYPE_ALIAS_TEMPLATE_DECL]

In [34]: len(kinds)
Out[34]: 160

```
