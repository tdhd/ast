from __future__ import print_function
import fnmatch
import os
import numpy as np
import clang.cindex
import pandas as pd
import scipy.spatial.distance
import zss


class SourceCodeParser(object):
    def __init__(self, root_path, file_pattern):
        self.root_path = root_path
        self.file_pattern = file_pattern

    @staticmethod
    def is_function(cursor):
        return True if cursor.kind == clang.cindex.CursorKind.FUNCTION_DECL else False

    @staticmethod
    def find_files(directory, pattern):
        for root, dirs, files in os.walk(directory):
            for basename in files:
                if fnmatch.fnmatch(basename, pattern):
                    filename = os.path.join(root, basename)
                    yield filename

    def read_functions(self):
        all_functions = []
        for filename in SourceCodeParser.find_files(self.root_path, self.file_pattern):
            print('Found source-file:', filename)
            index = clang.cindex.Index.create()
            translation_unit = index.parse(filename)
            functions = filter(lambda node: SourceCodeParser.is_function(node), translation_unit.cursor.walk_preorder())
            all_functions += functions

        print('Found', len(all_functions), 'functions')
        for fn in all_functions:
            print(fn.kind, fn.displayname)

        return all_functions


def optimal_costs_for(function, similars, dissimilars):
    import scipy.optimize


    def f(x):
        """
        cost function to minimize, parametrizes the zss costs

        :param x:
            x[0] = insert cost
            x[1] = removal cost
            x[2] = update cost
        :return:
        """
        return x[0]+x[1]+x[2]

    # todo: generate constraints from arguments
    cons = (
        {'type': 'ineq', 'fun': lambda x: x[0] - 2 * x[1] + 2},
        {'type': 'ineq', 'fun': lambda x: -x[0] + 2 * x[1] + 2}
    )

    result = scipy.optimize.minimize(
        f,
        x0=[1, 1, 1],
        bounds=((1,None),(1,None),(1,None)),
        constraints=cons
    )
    print(result)


def zss_edit_dist(first, second):
    def get_children(node):
        return list(node.get_children())

    def uniform_cost(node):
        # todo: depending on node.kind incur different costs
        return 1

    def update_cost(a, b):
        # todo: depending on nodes incur different costs
        return 1

    return zss.distance(
        first[0], second[0],
        get_children=get_children,
        insert_cost=uniform_cost,
        remove_cost=uniform_cost,
        update_cost=update_cost
    )


def normalized_zss_edit_dist(first, second):
    d = zss_edit_dist(first, second)
    return 2.0 * d / (len(list(first[0].walk_preorder())) + len(list(second[0].walk_preorder()))) - 1.0


def pairwise_distances():
    scp = SourceCodeParser('.', '*.c')
    all_functions = scp.read_functions()

    all_functions = pd.DataFrame({'node': all_functions})
    all_functions['displayname'] = all_functions.apply(lambda row: row.node.displayname, axis=1)
    all_functions['kind'] = all_functions.apply(lambda row: row.node.kind, axis=1)
    all_functions['displayname'] = all_functions.apply(lambda row: row.node.displayname, axis=1)
    all_functions['usr'] = all_functions.apply(lambda row: row.node.get_usr(), axis=1)
    all_functions['tkind'] = all_functions.apply(lambda row: [e.kind for e in row.node.get_tokens()], axis=1)
    all_functions['dlen'] = all_functions.apply(lambda row: len(row.node.data), axis=1)
    all_functions['children'] = all_functions.apply(lambda row: [(e.kind, e.displayname, e.result_type.get_size()) for e in row.node.get_children()], axis=1)
    all_functions['n_children'] = all_functions.apply(lambda row: len(list(row.node.get_children())), axis=1)
    all_functions['n_nodes'] = all_functions.apply(lambda row: len(list(row.node.walk_preorder())), axis=1)
    all_functions['n_args'] = all_functions.apply(lambda row: len(list(row.node.get_arguments())), axis=1)
    all_functions['lexical_parent_kind'] = all_functions.apply(lambda row: 'None' if row.node.lexical_parent is None else row.node.lexical_parent.kind, axis=1)
    all_functions['semantic_parent_kind'] = all_functions.apply(lambda row: 'None' if row.node.semantic_parent is None else row.node.semantic_parent.kind, axis=1)

    print(all_functions.head())
    print(all_functions.describe())
    similarity_mat = scipy.spatial.distance.squareform(
        scipy.spatial.distance.pdist(
            all_functions.node.values.reshape(-1, 1),
            normalized_zss_edit_dist
        )
    )
    print(similarity_mat, similarity_mat.max().max())
    most_similar_fn = all_functions.ix[np.argmax(similarity_mat.mean(axis=1))]
    print(most_similar_fn.displayname)
    least_similar_fn = all_functions.ix[np.argmin(similarity_mat.mean(axis=1))]
    print(least_similar_fn.displayname)


if __name__ == "__main__":

    scp = SourceCodeParser('.', '*.c')
    all_functions = scp.read_functions()

    for f in all_functions:
        print(f, len(list(f.get_arguments())))

    costs = optimal_costs_for(all_functions[0], [all_functions[1]], [all_functions[2]])

    similarity_mat = scipy.spatial.distance.squareform(
        scipy.spatial.distance.pdist(
            np.array(all_functions).reshape(-1, 1),
            normalized_zss_edit_dist
        )
    )
    print(similarity_mat)
    # pairwise_distances()
