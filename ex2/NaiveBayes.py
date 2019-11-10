import operator


class Model:
    def __init__(self, data, classes):
        self.data = data
        self.classes = classes

    def predict(self, inputs):
        '''
        predicting classification on single input
        '''
        freq = {c: 1.0 for c in self.classes}
        for c in self.classes:
            priors = []
            for _ in range(len(inputs[0])):
                priors.append(0.0)
            total = 0
            for features, label in self.data:
                if label == c:
                    total += 1
                    for i, f in enumerate(features):
                        if f == inputs[0][i]:
                            priors[i] += 1
            priors = [p / total for p in priors]
            for p in priors:
                freq[c] *= p
        return max(freq.items(), key=operator.itemgetter(1))[0]

    def toString(self):
        return "naiveBase"
