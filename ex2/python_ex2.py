import KNN
import DecisionTree
import NaiveBayes
import operator
import os

F2I = {}  # maps features to indexes
I2F = {}  # maps indexes to features
L2I = {}  # maps classification-labels to indexes
I2L = {}  # maps indexes to classification-labels
V2I = {}  # maps feature-values to indexes
I2V = {}  # maps indexes to feature-values


def pre_process(filename):
    '''
    mapping all the features, their values and classifications to indexes and vice versa
    '''
    global F2I, I2F, L2I, I2L, V2I, I2V
    n = 1
    for line in open(filename):
        l = line.split()
        if n == 1:
            # extracting names of the features
            F2I = {f: i for i, f in enumerate(l[:-1])}
            I2F = {i: f for i, f in enumerate(l[:-1])}
            for i, f in enumerate(l[:-1]):
                V2I[I2F[i]] = {}
        else:
            # extracting the classification-labels
            if l[-1] not in L2I:
                L2I[l[-1]] = len(L2I)
            # extracting the feature-values
            for i, f in enumerate(l[:-1]):
                if f not in V2I[I2F[i]]:
                    V2I[I2F[i]][f] = len(V2I[I2F[i]])
        n += 1

    I2L = {i: l for l, i in L2I.items()}
    I2V = {}
    for f, v2i in V2I.items():
        I2V[F2I[f]] = {i: v for v, i in v2i.items()}


def get_data(filename):
    '''
    reading the data from a given file
    @param filename: name of the file
    '''
    data = []

    n = 1
    for line in open(filename):
        if n == 1:
            n += 1
            continue
        l = line.split()
        x = [V2I[I2F[i]][f] for i, f in enumerate(l[:-1])]
        y = L2I[l[-1]]
        data.append((x, y))

    return data


def predict(TEST, algorithms):
    '''
    predicting the classifications on the given examples with each one of the algorithms given
    @param TEST: list of examples
    @param algorithms: list of algorithms
    '''
    title = "Num"
    for alg in algorithms:
        title += "\t{}".format(alg.toString())
    r.write("{}\n".format(title))
    accu = [0.0 for _ in range(len(algorithms))]
    for i, inputs in enumerate(TEST):
        pred = "{}".format(i + 1)
        for j, alg in enumerate(algorithms):
            y_hat = alg.predict(inputs)
            accu[j] += y_hat == inputs[1]
            pred += "\t{}".format(I2L[y_hat])
        r.write("{}\n".format(pred))

    accu = [a / len(TEST) for a in accu]
    out = ""
    for a in accu:
        out += "\t{:.2f}".format(a)
    r.write("{}".format(out))


def print_tree(root, tabs=""):
    '''
    printing the tree the DecisionTree algorithm ceated
    @param root: the root of the (sub)tree
    @param tabs: string containing desired tabs
    '''
    # retrieving tuples of value:node
    next = [(I2V[root.attribute][value], node) for value, node in root.next.items()]
    # alphabetically sorting by value
    next.sort(key=operator.itemgetter(0))

    for value, n in next:
        if n.classification is None:
            t.write("{}{}={}\n".format(tabs, I2F[root.attribute], value))
            print_tree(n, tabs.split('|')[0] + "\t|")
        else:
            t.write("{}{}={}:{}\n".format(tabs, I2F[root.attribute], value, I2L[n.classification]))


if __name__ == "__main__":
    # pre-processing the data
    pre_process("train.txt")
    '''
    print(F2I)
    print(I2F)
    print(L2I)
    print(I2L)
    print(V2I)
    print(I2V)
    '''
    # loading the datasetes
    TRAIN = get_data("train.txt")
    TEST = get_data("test.txt")

    # creating the different models
    dt = DecisionTree.Model(TRAIN, values={i: value.keys() for i, value in I2V.items()})
    knn = KNN.Model(TRAIN, k=5)
    nb = NaiveBayes.Model(TRAIN, I2L.keys())

    # predicting on the TEST set
    r = open("output.txt", 'w')
    predict(TEST, [dt, knn, nb])
    r.close()

    # printing the tree that DecisionTree created
    t = open("output_tree.txt", 'w')
    print_tree(dt.root)
    t.close()

    # deleting the last '\n' character
    with open("output_tree.txt", 'rb+') as t:
        t.seek(-1, os.SEEK_END)
        t.truncate()
