import collections
import numpy as np


def normalized_levenshtein(s1, s2, insert_cost=1, removal_cost=1, update_cost=1, symbol_costs=collections.OrderedDict()):
    d = levenshtein(s1, s2, insert_cost, removal_cost, update_cost, symbol_costs)
    return 1.0*d/max(len(s1), len(s2))


def levenshtein(s1, s2, insert_cost=1, removal_cost=1, update_cost=1, symbol_costs=collections.OrderedDict()):
    if len(s1) < len(s2):
        return levenshtein(s2, s1, insert_cost, removal_cost, update_cost, symbol_costs)

    # len(s1) >= len(s2)
    if len(s2) == 0:
        return len(s1)

    previous_row = range(len(s2) + 1)
    for i, c1 in enumerate(s1):
        current_row = [i + 1]
        for j, c2 in enumerate(s2):
            # insertions = previous_row[j + 1] + insert_cost  # j+1 instead of j since previous_row and current_row are one character longer
            # deletions = current_row[j] + removal_cost  # than s2
            # substitutions = previous_row[j] + update_cost * (c1 != c2)

            # print(c1, c2)
            cost1, cost2 = symbol_costs[c1], symbol_costs[c2]
            cost = (cost1 + cost2) / 2.0
            insertions = previous_row[j + 1] + cost  # j+1 instead of j since previous_row and current_row are one character longer
            deletions = current_row[j] + cost  # than s2
            substitutions = previous_row[j] + cost * (c1 != c2)

            current_row.append(min(insertions, deletions, substitutions))
        previous_row = current_row

    return previous_row[-1]
