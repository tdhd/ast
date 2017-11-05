def first(all):
    for entry in all:
        print(entry)


def cumsum(numbers):
    i = 0
    for number in numbers:
        i+=number
    return i


def mean(numbers):
    i = 0
    for number in numbers:
        i+=number
    return 1.0*i/len(numbers)
