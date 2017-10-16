import clang.cindex
import collections
import pandas as pd
import numpy as np


import os, fnmatch


def find_files(directory, pattern):
    for root, dirs, files in os.walk(directory):
        for basename in files:
            if fnmatch.fnmatch(basename, pattern):
                filename = os.path.join(root, basename)
                yield filename


for filename in find_files('/home/ppschmidt/linux-stable', '*.c'):
    print 'Found C source:', filename
    hist = collections.defaultdict(int)
    #children_counts = collections.defaultdict(int)

    nodes = []

    index = clang.cindex.Index.create()
    tu = index.parse(filename)

    q = []

    q.append(tu.cursor)

    while len(q) > 0:
        e = q[0]
        #print e, e.location, e.kind, len(list(e.get_children()))
        hist[e.kind] += 1
        nodes.append(e)
        #children_counts[e.kind] += len(list(e.get_children()))
        for child in e.get_children():
            q.append(child)
        del q[0]


    # TODO: tu.cursor.walk_preorder()
    # clang.cindex.CursorKind.
    s = pd.DataFrame({'node': nodes})
    s['kind'] = s.apply(lambda row: row.node.kind, axis=1)
    s['n_children'] = s.apply(lambda row: len(list(row.node.get_children())), axis=1)
    s['n_args'] = s.apply(lambda row: len(list(row.node.get_arguments())), axis=1)
    s['lexical_parent_kind'] = s.apply(lambda row: 'None' if row.node.lexical_parent is None else row.node.lexical_parent.kind, axis=1)
    s['semantic_parent_kind'] = s.apply(lambda row: 'None' if row.node.semantic_parent is None else row.node.semantic_parent.kind, axis=1)
    #print s
    grouped = s.groupby(['kind']).agg({'kind': np.size}).sort_values('kind', ascending=False)
    #grouped = s.groupby(['kind', 'lexical_parent_kind']).agg({'kind': np.size})
    #grouped = s.groupby(['kind', 'lexical_parent_kind', 'semantic_parent_kind']).agg({'kind': np.size}).sort_values('kind', ascending=False)
    grouped['p'] = grouped.kind/grouped.kind.sum()
    del grouped['kind']
    print grouped

    print '-'*100

    '''
    grouped = s.groupby('kind').agg({'n_children': np.average})
    print grouped.sort_values('n_children', ascending=False)
    #print sorted(hist.iteritems(),key=lambda (k,v): v,reverse=True)
    histdf = pd.DataFrame(sorted(hist.iteritems(),key=lambda (k,v): v,reverse=True))
    histdf.columns = ['kind', 'n']
    histdf['p'] = histdf.n/histdf.n.sum()
    histdf['cp'] = histdf.p.cumsum()
    print histdf.head(25)
    '''

