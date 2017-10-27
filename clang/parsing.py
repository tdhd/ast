import fnmatch
import os

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
            print 'Found source-file:', filename
            index = clang.cindex.Index.create()
            translation_unit = index.parse(filename)
            functions = filter(lambda node: SourceCodeParser.is_function(node), translation_unit.cursor.walk_preorder())
            all_functions += functions

        print 'Found', len(all_functions), 'functions'
        for fn in all_functions:
            print fn.kind, fn.displayname

        return all_functions


if __name__ == "__main__":
    def edit_dist(first, second):
        print first
        print second
        return 0


    def zss_edit_dist(first, second):
        def get_children(node):
            return list(node.get_children())

        def uniform_cost(node):
            return 1

        def update_cost(a, b):
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


    scp = SourceCodeParser('.', '*.c')
    all_functions = scp.read_functions()

    all_functions = pd.DataFrame({'function': all_functions}).function.values.reshape(-1, 1)
    similarity_mat = scipy.spatial.distance.squareform(
        scipy.spatial.distance.pdist(
            all_functions,
            normalized_zss_edit_dist
        )
    )
    print similarity_mat
