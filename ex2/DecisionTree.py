import operator
import math


class Node:
    def __init__(self, attribute=None, classification=None):
        self.attribute = attribute
        self.classification = classification
        self.next = {}

    def add_child(self, value, node):
        self.next[value] = node


class Model:
    def __init__(self, examples, values):
        self.values = values
        self.root = self.DTL(examples, values.keys(), self.best_classification(examples))

    def DTL(self, examples, attributes, default):
        # ID3 algorithm
        if len(examples) == 0:
            return Node(classification=default)
        if self.same_classification(examples):
            return Node(classification=examples[0][1])
        if len(attributes) == 0:
            return Node(classification=self.best_classification(examples))

        best = self.choose_attribute(examples, attributes)
        tree = Node(attribute=best)
        for v_i in self.values[best]:
            examples_i = []
            for inputs, label in examples:
                if inputs[best] == v_i:
                    examples_i.append((inputs, label))
            t = [a for a in attributes if a != best]
            subtree = self.DTL(examples_i, t, self.best_classification(examples))
            tree.add_child(v_i, subtree)

        return tree

    def choose_attribute(self, examples, attributes):
        '''
        chooses the best attribute for the list of examples
        @param examples: list of examples
        @param attributes: list of (available) attributes
        '''
        E = self.entropy(examples)
        gains = {}
        for attribute in attributes:
            part_entropy = 0.0
            for v_i in self.values[attribute]:
                examples_i = []
                for inputs, label in examples:
                    if inputs[attribute] == v_i:
                        examples_i.append((inputs, label))
                p = float(len(examples_i)) / len(examples)
                part_entropy += p * self.entropy(examples_i)
            gains[attribute] = E - part_entropy

        return max(gains.items(), key=operator.itemgetter(1))[0]

    def entropy(self, examples):
        '''
        calculates and returns the entropy on the list of examples
        @param examples: list of examples
        '''
        freq = {}
        for _, label in examples:
            if label in freq:
                freq[label] += 1
            else:
                freq[label] = 1
        freq = {l: float(f) / len(examples) for l, f in freq.items()}
        return sum([-1.0 * p * math.log(p) / math.log(2) if p != 0 else 0 for p in freq.values()])

    def same_classification(self, examples):
        '''
        checks wether all the examples have the same classification
        '''
        base = examples[0][1]
        for ex in examples:
            if base != ex[1]:
                return False
        return True

    def best_classification(self, examples):
        '''
        finds the best classification for the set of examples (majority wins)
        '''
        counts = {}
        for _, l in examples:
            if l in counts:
                counts[l] += 1
            else:
                counts[l] = 0
        return max(counts.items(), key=operator.itemgetter(1))[0]

    def predict(self, inputs):
        '''
        predicting classification on single input
        '''
        c = self.root
        while c.classification == None:
            c = c.next[inputs[0][c.attribute]]
        return c.classification

    def get_tree_root(self):
        return self.root

    def toString(self):
        return "DT"