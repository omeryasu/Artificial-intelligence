import operator


class Model:
    def __init__(self, data, k=5):
        self.data = data
        self.k = k

    def predict(self, inputs):
        '''
        predicting classification on single input
        '''
        distances = []
        for i, n in enumerate(self.data):
            dist = HammingDistance(inputs[0], n[0])
            distances.append((i, dist))
        # sorting according the distance
        distances.sort(key=operator.itemgetter(1))

        # n[0] gives us the index to an example in data
        # data[n[0]] gives us the example itself
        # data[n[0]][1] gives the label of the example
        neighbors = []
        for n in distances[:self.k]:
            neighbors.append(self.data[n[0]][1])

        frequencies = {}
        # calculating the frequencies of each label
        for label in neighbors:
            if label not in frequencies:
                frequencies[label] = 1
            else:
                frequencies[label] += 1
        return max(frequencies.items(), key=operator.itemgetter(1))[0]

    def toString(self):
        return "KNN>"


def HammingDistance(a, b):
    return sum([1 for i, j in zip(a, b) if i != j])
