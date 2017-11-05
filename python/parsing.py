import numpy as np
import pandas as pd
import sklearn.covariance
from redbaron import RedBaron


def metric_learn_histogram(function, similars, dissimilars, empirical_loss_weight=1.0):
    """
    learn diagonal distance matrix.
    :param function:
    :param similars:
    :param dissimilars:
    :param empirical_loss_weight:
    :return:
    """
    import scipy.optimize

    def f(x):
        x_ = np.array(x)
        mean_distance_similars = np.sum([x_.dot(np.abs(function - similar)) for similar in similars])
        mean_distance_dissimilars = np.sum([x_.dot(np.abs(function - dissimilar)) for dissimilar in dissimilars])
        obj = empirical_loss_weight * (mean_distance_similars - mean_distance_dissimilars) + np.linalg.norm(x)
        print(mean_distance_similars, mean_distance_dissimilars, obj)
        return obj

    min = 1
    max = 2
    result = scipy.optimize.minimize(
        f,
        x0=[1 for _ in range(function.shape[0])],
        bounds=[(min, max) for _ in range(function.shape[0])],
        tol=1e-3,
        method='L-BFGS-B'
    )
    print(result)
    return result.x


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

metric_learn_histogram(X[0, :], X[1:2, :], X[2:, :])
