import time
import matplotlib.pyplot as plt
import numpy as np


class AntColony:
    ITERATION_LIMIT = 100
    ANTS_AMOUNT = 50
    ELITE_ANTS_AMOUNT = 1
    WILD_ANTS_AMOUNT = 2
    EVAPORATION_RATE = 0.2
    ALPHA = 1
    BETA = 1
    L_MIN = 1500
    EARLY_STOPPING_COUNT = round(ITERATION_LIMIT * 0.7)
    START_FROM_DIFFERENT_POINTS = True

    def __init__(self):
        self.pheromone_matrix = None
        self.visibility_matrix = None
        self.probability_matrix = None
        self.map = None
        self.available_nodes = None
        self.fit_time = None
        self.stopped_early = False
        self.best_score = float('inf')
        self.best_path = None
        self.all_best_scores = []

    def fit(self, map_matrix):
        self.map = map_matrix
        start = time.time()
        self.initialize()
        #################################
        num_equal = 0
        for i in range(self.ITERATION_LIMIT):
            start_iter = time.time()
            all_paths = []
            ant_type = "default"
            for ant in range(self.ANTS_AMOUNT + self.WILD_ANTS_AMOUNT):
                if ant >= self.ANTS_AMOUNT:
                    ant_type = "wild"
                path = []
                if self.START_FROM_DIFFERENT_POINTS: #Початок з різних точок чи навпаки
                    current_node = self.available_nodes[np.random.randint(0, len(self.available_nodes))]
                else:
                    current_node = self.available_nodes[0]
                start_node = current_node
                
                while True:
                    path.append(current_node)
                    self.available_nodes.remove(current_node)
                    if len(self.available_nodes) != 0:
                        current_node_index = self.Next_node(current_node, ant_type)
                        current_node = self.available_nodes[current_node_index]
                    else:
                        break

                path.append(start_node)  # Назад в початкову точку
                self.available_nodes = list(range(self.map.shape[0]))
                all_paths.append(path)

            best_path, best_score, all_scores = self.Stats_for_best(all_paths)#Статистика

            if i == 0:
                self.best_score = best_score
                self.best_path = best_path
            else:
                if best_score < self.best_score:
                    self.best_score = best_score
                    self.best_path = best_path
            if best_score == self.best_score:
                num_equal += 1
            else:
                num_equal = 0
            self.all_best_scores.append(best_score)
            self.evaporation()#Підрахунок випаровування
            self.lvl_pheromone(all_scores, all_paths)#Інтенсивність або ж рівень феромону
            self.Probabilities()

            print("Найкраща відстань на ітерації {}: {};".format(i, round(best_score)))#({:.2f}s), time.time() - start_iter

        self.fit_time = (time.time() - start)
        print("Найкраща відстань: {}. \n".format(self.best_score))#{} round(self.fit_time, 2) {} self.best_path
        return self.best_score

    def initialize(self):
        #Ініціалізує модель, створюючи різні матриці та генеруючи список доступних вузлів
        num_nodes = self.map.shape[0]
        self.available_nodes = list(range(num_nodes))
        self.pheromone_matrix = np.zeros((num_nodes, num_nodes)) #Матриця заповнена нулями
        # видалити діагональ, оскільки немає феромону від вузла i до самого себе
        self.pheromone_matrix[np.eye(num_nodes) == 0] = 0.1
        self.visibility_matrix = 1 / self.map
        self.probability_matrix = np.zeros((num_nodes, num_nodes))
        
        self.Probabilities() #Функція розрахунку P ймовірність переходу шляхом

    def Probabilities(self):
        #Отримання матриці ймовірностей переходу в сусідню вершину
        num_nodes = self.map.shape[0]
        for i in range(num_nodes):
            for j in range(num_nodes):
                numerator = self.pheromone_matrix[i][j] ** self.ALPHA * self.visibility_matrix[i][j] ** self.BETA
                denominator = 0
                for k in range(num_nodes):
                    denominator += self.pheromone_matrix[i][k] ** self.ALPHA * self.visibility_matrix[i][k] ** self.BETA
                self.probability_matrix[i][j] = numerator / denominator

    def Next_node(self, from_node, ant_type="default"):
        #Вибирає наступний вузол на основі ймовірностей
        probabilities = self.probability_matrix[from_node, self.available_nodes]
        if ant_type == "default":
            probabilities /= sum(probabilities)
            next_node = np.random.choice(range(len(probabilities)), p=probabilities)
        else:
            next_node = np.random.choice(range(len(probabilities)))
        return next_node

    def Stats_for_best(self, paths):
        #Оцінює рішення мурашок, додаючи відстані між вузлами.
        best = float('inf')
        scores = np.zeros(len(paths))
        for index, path in enumerate(paths):
            score = 0
            for i in range(len(path) - 1):
                score += self.map[path[i], path[i + 1]]
            scores[index] = score
            best = np.argmin(scores)
        return paths[best], scores[best], scores

    def evaporation(self):
        #Випаровування феромону.
        self.pheromone_matrix *= (1 - self.EVAPORATION_RATE)

    def lvl_pheromone(self, scores, paths):
        #Збільшення феромону на пройдених шляхах, і на кращому шляху, якщо присутні елітні мурахи.
        i = self.best_path[:-1]
        j = self.best_path[1:]
        self.pheromone_matrix[i, j] += self.ELITE_ANTS_AMOUNT * (self.L_MIN / self.best_score)
        for index, score in enumerate(scores):
            i = paths[index][:-1]
            j = paths[index][1:]
            self.pheromone_matrix[i, j] += self.L_MIN / score


    def Create_graph(self):
        fig, ax = plt.subplots(figsize=(10, 5))
        ax.plot(self.all_best_scores, label="Best Run")
        ax.set_xlabel("Ітерація")
        ax.set_ylabel("Відстань")
        plt.show()


A = np.random.randint(5, 150, size=(50, 50))
MyAntColony = AntColony()
best = MyAntColony.fit(A)
#MyAntColony.Create_graph()

