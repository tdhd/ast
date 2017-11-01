from __future__ import print_function

import collections
import fnmatch
import os

import clang.cindex
import numpy as np
import pandas as pd
import scipy.spatial.distance
import zss

import editdistance


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


def zss_edit_dist(first, second, insert_cost, remove_cost, update_cost):
    def get_children(node):
        return list(node.get_children())

    return zss.distance(
        first, second,
        get_children=get_children,
        insert_cost=insert_cost,
        remove_cost=remove_cost,
        update_cost=update_cost
    )


def normalized_zss_edit_dist(first, second, insert_cost, remove_cost, update_cost):
    d = zss_edit_dist(first, second, insert_cost, remove_cost, update_cost)
    # todo: adapt for custom costs for normalization
    return 2.0 * d / (len(list(first.walk_preorder())) + len(list(second.walk_preorder()))) - 1.0
    # return d


def optimal_costs_for_linearization(linearized_fn, similars, dissimilars, objective_weight_similars=1.0, symbol_costs=collections.OrderedDict()):
    import scipy.optimize

    def f(x):
        """
        cost function to minimize, parametrizes the zss costs

        :param x:
            x[0] = insert cost
            x[1] = removal cost
            x[2] = update cost
        :param objective_weight_similars: real, > 0, steers influence of similar and dissimilar in objective.
        :return:
        """
        for key, parameter in zip(symbol_costs, x):
            symbol_costs[key] = parameter

        mean_distance_similars = np.sum(
            [editdistance.normalized_levenshtein(linearized_fn, similar, x[0], x[1], x[2], symbol_costs) for similar in similars]
        )
        mean_distance_dissimilars = np.sum(
            [editdistance.normalized_levenshtein(linearized_fn, dissimilar, x[0], x[1], x[2], symbol_costs) for dissimilar in dissimilars]
        )
        obj = objective_weight_similars * mean_distance_similars - mean_distance_dissimilars# + np.linalg.norm(x)
        print(mean_distance_similars, mean_distance_dissimilars, obj)
        # print(obj)
        return obj

    min = 1
    max = 2
    cons = (
        {
            'type': 'eq',
            'fun': lambda x: np.linalg.norm(x) == 1.0
        }
    )
    result = scipy.optimize.minimize(
        f,
        x0=[symbol_costs[k] for k in symbol_costs],
        bounds=[(min, max) for _ in range(len(symbol_costs))],
        tol=1e-2,
        method='L-BFGS-B'
        # constraints=cons
    )
    print(result)
    return result.x


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
        print('current costs', x)
        insert_cost = lambda node: x[0]
        remove_cost = lambda node: x[1]
        update_cost = lambda node_a, node_b: x[2]
        mean_distance_similars = np.average(
            [normalized_zss_edit_dist(function, similar, insert_cost, remove_cost, update_cost) for similar in similars]
        )
        mean_distance_dissimilars = np.average(
            [normalized_zss_edit_dist(function, dissimilar, insert_cost, remove_cost, update_cost) for dissimilar in dissimilars]
        )
        print(mean_distance_similars, mean_distance_dissimilars)
        obj = mean_distance_similars - mean_distance_dissimilars
        return obj

    # todo: generate constraints from arguments
    cons = (
        {
            'type': 'ineq',
            'fun': lambda x:
                normalized_zss_edit_dist(function, similars[0], lambda n: x[0], lambda n: x[1], lambda a,b: x[2]) -
                normalized_zss_edit_dist(function, dissimilars[0], lambda n: x[0], lambda n: x[1], lambda a,b: x[2])
        }
    )

    result = scipy.optimize.minimize(
        f,
        x0=[1, 1, 1],
        bounds=((1,None),(1,None),(1,None)),
        tol=1e-24,
        constraints=cons
    )
    print(result)
    return result.x


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
    import copy
    symbol_costs = collections.OrderedDict()
    kind2hex = {}
    for idx, kind in enumerate([e for e in clang.cindex.CursorKind._kinds if e is not None]):
        kind2hex[kind] = chr(idx)
        symbol_costs[chr(idx)] = 1.5 #np.random.rand() + 1.0
    orig_symbol_costs = copy.deepcopy(symbol_costs)
    print(len(kind2hex))

    def linearization(node):
        return ','.join(map(lambda n: kind2hex[n.kind], list(node.walk_preorder())))


    # print(editdistance.levenshtein("hello", "world", update_cost=1, insert_cost=2, removal_cost=2))
    # print(editdistance.normalized_levenshtein("aaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaab"))
    # print(
    #     editdistance.levenshtein(
    #         ''.join([chr(i) for i in xrange(256)]),
    #         ''.join([chr(i) for i in xrange(128)])
    #     )
    # )

    scp = SourceCodeParser('src', '*.c')
    all_functions = scp.read_functions()

    fn_idx = 34
    linearized_optimal_costs = optimal_costs_for_linearization(
        linearization(all_functions[fn_idx]),
        map(linearization, all_functions[1:30]),
        map(linearization, all_functions[30:33]),
        objective_weight_similars=1,
        symbol_costs=symbol_costs
    )
    print(linearized_optimal_costs)

    updated_symbol_costs = collections.OrderedDict([(key, value) for key, value in zip(symbol_costs, linearized_optimal_costs)])
    edit_dists = map(
        lambda f: editdistance.normalized_levenshtein(
            linearization(all_functions[fn_idx]),
            linearization(f),
            # symbol_costs=orig_symbol_costs  #.94, .98
            symbol_costs=updated_symbol_costs  #.83, .78
        ),
        all_functions[1:33]
    )

    print(all_functions[fn_idx].displayname)
    for ed, name in zip(edit_dists, map(lambda f: f.displayname, all_functions[1:33])):
        print(ed, name)
    print(np.average(edit_dists[1:30]))
    print(np.average(edit_dists[30:33]))
    edit_dists = None

    # print(
    #     '---',
    #     editdistance.levenshtein(
    #         linearization(all_functions[0]),
    #         linearization(all_functions[1]),
    #         removal_cost=1,
    #         insert_cost=1.0001,
    #         update_cost=1
    #     )
    # )
    #
    # for f in all_functions:
    #     print(','.join(map(lambda n: str(n.kind).split(".")[-1], list(f.walk_preorder()))))
    #     print(f, len(list(f.get_arguments())))
    #
    # costs = optimal_costs_for(all_functions[0], [all_functions[1]], [all_functions[2]])
    # print(costs)
    # # similarity_mat = scipy.spatial.distance.squareform(
    # #     scipy.spatial.distance.pdist(
    # #         np.array(all_functions).reshape(-1, 1),
    # #         normalized_zss_edit_dist
    # #     )
    # # )
    # # print(similarity_mat)
    # # # pairwise_distances()
