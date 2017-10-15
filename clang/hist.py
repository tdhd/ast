import clang.cindex
import collections


#hist = collections.defaultdict(lambda: collections.defaultdict(dict))
hist = collections.defaultdict(int)

index = clang.cindex.Index.create()
tu = index.parse("test.c")

q = []

q.append(tu.cursor)

while len(q) > 0:
    e = q[0]
    print e, e.location, e.kind, len(list(e.get_children()))
    #hist[e.kind] += 1
    hist[len(list(e.get_children()))] += 1
    for child in e.get_children():
        q.append(child)
    del q[0]

print hist

print sorted(hist.iteritems(),key=lambda (k,v): v,reverse=True)

