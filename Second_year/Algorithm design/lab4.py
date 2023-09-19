from copy import copy
from random import random
from random import randint


class Genetic:
    BACKPACK_CAPACITY = 150
    NUMBER_OF_ITEMS = 100
    ITERATION_LIMIT = 1000
    mutation_probability = 0.05
    PATH_TO_FILE = "my_file.txt"
    weight = [randint(1, 5) for _ in range(NUMBER_OF_ITEMS)]
    value = [randint(2, 10) for _ in range(NUMBER_OF_ITEMS)]
    population = []
    probabilities = []
    best = None
    best_ = None

    def start(self):
        data_ = ""
        self.CreatePopulation()
        for i in range(self.ITERATION_LIMIT):
            if (i + 1) % 20 == 0:
                data_ += f"({i + 1}, {self.population[-1][-1]})\n"
            self.Probabilities_()
            self.NewGeneration()

        weight = 0
        for i in range(self.NUMBER_OF_ITEMS):
            if self.population[-1][0][i] == 1:
                weight += self.weight[i]
        print("Weight: ", weight)
        print("Value: ", self.population[-1][-1])
        self.in_file(data_)

    @staticmethod
    def mutation(chromosome):
        mutated_chromosome = copy(chromosome)
        i1 = randint(0, len(chromosome) - 1)
        i2 = randint(0, len(chromosome) - 1)
        mutated_chromosome[i1], mutated_chromosome[i2] = mutated_chromosome[i2], mutated_chromosome[i1]
        return mutated_chromosome

    @staticmethod
    def crossover(parent_1, parent_2):
        child_chromosome = []
        for i in range(len(parent_1)):
            if parent_1[i] != parent_2[i]:
                new_gene = randint(0, 1)
                child_chromosome.append(new_gene)
            else:
                child_chromosome.append(parent_1[i])
        return child_chromosome

    def CreatePopulation(self):
        for i in range(self.NUMBER_OF_ITEMS):
            init = [0] * self.NUMBER_OF_ITEMS
            init[i] = 1
            self.population.append((init, self.fitness(init)))
        return True

    def fitness(self, chromosome):
        sum_of_weights = 0
        sum_of_values = 0
        for i in range(len(chromosome)):
            if chromosome[i] == 1:
                sum_of_weights += self.weight[i]
                sum_of_values += self.value[i]
        if sum_of_weights > self.BACKPACK_CAPACITY:
            return 0
        else:
            return sum_of_values

    def Probabilities_(self):
        self.probabilities = []
        total_sum = 0
        for chromosome in self.population:
            total_sum += chromosome[1]
        probability_sum = 0
        for chromosome in self.population:
            if total_sum == 0:
                probability_sum = 0
            else:
                probability_sum += float(chromosome[1] / total_sum)
            self.probabilities.append(probability_sum)

    def NewGeneration(self):
        parent_1 = 0
        parent_2 = -1
        r = random()
        for parent in range(len(self.probabilities)):
            if r < self.probabilities[parent]:
                parent_1 = parent
                break
        r = random()
        for parent in range(len(self.probabilities)):
            if r < self.probabilities[parent]:
                parent_2 = parent
                break

        child_chromosome = self.crossover(self.population[parent_1][0], self.population[parent_2][0])
        r = random()
        if r < self.mutation_probability:
            mutated_chromosome = self.mutation(child_chromosome)
            final_chromosome = self.upgraded_chromosome(mutated_chromosome)
        else:
            final_chromosome = self.upgraded_chromosome(child_chromosome)
        self.population.append((final_chromosome, self.fitness(final_chromosome)))
        self.population.sort(key=lambda x: x[-1])
        self.population.pop(0)

    def upgraded_chromosome(self, chromosome):
        upgraded_chromosome = chromosome.copy()
        while True:
            i = randint(0, self.NUMBER_OF_ITEMS - 1)
            if upgraded_chromosome[i] == 0:
                upgraded_chromosome[i] = 1
                break
        return upgraded_chromosome

    def in_file(self, data_):
        final_weight = 0
        result = ''
        for i in range(self.NUMBER_OF_ITEMS):
            result += f"item {i + 1}: ( weight {self.weight[i]}, values {self.value[i]})\n"
        result += "---------------------------------\n"
        for i in range(self.NUMBER_OF_ITEMS):
            if self.population[-1][0][i] == 1:
                result += f"item {i + 1}: ( weight {self.weight[i]}, values {self.value[i]})\n"
                final_weight += self.weight[i]
        result += "---------------------------------\n"
        result += data_
        with open(self.PATH_TO_FILE, "wt") as my_file:
            my_file.write(result)


g = Genetic()
g.start()
