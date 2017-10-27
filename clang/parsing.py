import clang.cindex
import os, fnmatch
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
        # TODO
        return 1

    scp = SourceCodeParser('.', '*.c')
    all_functions = scp.read_functions()

    all_functions = pd.DataFrame({'function': all_functions}).function.values.reshape(-1, 1)
    similarity_mat = scipy.spatial.distance.squareform(scipy.spatial.distance.pdist(all_functions, edit_dist))
    print similarity_mat
