import numpy as np
from redbaron import RedBaron
import pandas as pd
import sklearn.feature_extraction.text
import sklearn.covariance

red = RedBaron(open('sample_src/test.py').read())
# print(red.dumps())
defs = red.find_all('DefNode')
print(len(defs))


def hist_for(node):
    return list(map(lambda n: str(type(n)).split(".")[-1][:-2], node.find_all(lambda x: True)))


function_nodes = []

for d in defs:
    function_nodes.append(' '.join(hist_for(d)))

df = pd.DataFrame({'nodes': function_nodes})
print(df.head())

cv = sklearn.feature_extraction.text.CountVectorizer(
    lowercase=False
)

X = cv.fit_transform(df.nodes).toarray()
print(X)
print(cv.vocabulary_)
print(len(cv.vocabulary_))

cov = sklearn.covariance.EmpiricalCovariance()
cov.fit(X)
print(cov.covariance_)
print(np.linalg.matrix_rank(cov.covariance_), cov.covariance_.shape)
