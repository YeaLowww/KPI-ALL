import numpy as np
import matplotlib.pyplot as plt

def create_input_data():
    characters = [
        np.array([
            0, 0, 0, 0, 0, 0,
            0, 0, 1, 0, 0, 0,
            0, 0, 1, 0, 0, 0,
            1, 1, 1, 1, 1, 1,
            0, 0, 1, 0, 0, 0,
            0, 0, 1, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
        ]).reshape(8, 6),
        np.array([
            0, 1, 0, 1, 0, 0,
            0, 1, 0, 1, 0, 0,
            0, 1, 0, 1, 0, 0,
            1, 1, 1, 1, 1, 1,
            0, 1, 0, 1, 0, 0,
            1, 1, 1, 1, 1, 1,
            0, 1, 0, 1, 0, 0,
            0, 1, 0, 1, 0, 0,
        ]).reshape(8, 6),
        np.array([
            0, 0, 1, 1, 0, 0,
            0, 1, 0, 0, 1, 0,
            0, 1, 0, 0, 1, 0,
            0, 0, 1, 1, 0, 0,
            0, 1, 0, 0, 1, 1,
            1, 0, 0, 0, 1, 0,
            1, 0, 0, 0, 1, 0,
            0, 1, 1, 1, 0, 0,
        ]).reshape(8, 6),
        np.array([
            0, 0, 1, 1, 0, 0,
            0, 1, 0, 0, 1, 0,
            0, 1, 0, 0, 1, 0,
            0, 1, 0, 0, 1, 0,
            0, 1, 0, 0, 1, 0,
            0, 1, 0, 0, 1, 0,
            0, 1, 0, 0, 1, 0,
            0, 0, 1, 1, 0, 0,
        ]).reshape(8, 6),
        np.array([
            0, 0, 0, 1, 0, 0,
            0, 0, 1, 1, 0, 0,
            0, 1, 0, 1, 0, 0,
            0, 0, 0, 1, 0, 0,
            0, 0, 0, 1, 0, 0,
            0, 0, 0, 1, 0, 0,
            0, 0, 0, 1, 0, 0,
            0, 1, 1, 1, 1, 1,
        ]).reshape(8, 6),
        np.array([
            0, 0, 0, 1, 1, 0,
            0, 0, 1, 0, 0, 1,
            0, 1, 0, 0, 0, 0,
            0, 1, 0, 0, 0, 0,
            0, 1, 0, 0, 0, 0,
            0, 1, 0, 0, 0, 1,
            0, 0, 1, 1, 1, 0,
            0, 0, 0, 0, 0, 0,
        ]).reshape(8, 6),
        np.array([
            0, 0, 1, 1, 0, 0,
            0, 0, 0, 0, 1, 0,
            0, 0, 0, 0, 1, 0,
            0, 0, 0, 1, 1, 0,
            0, 0, 0, 0, 1, 0,
            0, 0, 0, 0, 1, 0,
            0, 0, 1, 1, 0, 0,
            0, 0, 0, 0, 0, 0,
        ]).reshape(8, 6)
    ]
    
    # Visualization
    fig, axs = plt.subplots(2, 4, figsize=(12, 6))
    for i, ax in enumerate(axs.flatten()):
        if i < len(characters):  # Ensure the index is within the range of characters
            ax.imshow(characters[i], cmap='binary')
            ax.axis('off')
        else:
            ax.axis('off')  # Turn off the axis for empty subplots
    plt.tight_layout()
    plt.show()

    # Input part of the training DataSet array
    input_data = [char.reshape(1, 48) for char in characters]

    return input_data


def create_output_data():
    output_data = [
        [1, 0, 0, 0, 0, 0, 0],
        [0, 1, 0, 0, 0, 0, 0],
        [0, 0, 1, 0, 0, 0, 0],
        [0, 0, 0, 1, 0, 0, 0],
        [0, 0, 0, 0, 1, 0, 0],
        [0, 0, 0, 0, 0, 1, 0],
        [0, 0, 0, 0, 0, 0, 1]
    ]
    return np.array(output_data)


def sigmoid(x):
    return 1 / (1 + np.exp(-x))


# Побудова нейронної мережі
def forward_propagation(x, weights1, weights2):
    # Прихований шар
    hidden_inputs = x.dot(weights1)
    hidden_outputs = sigmoid(hidden_inputs)

    # Вихідний шар
    final_inputs = hidden_outputs.dot(weights2)
    final_outputs = sigmoid(final_inputs) 

    return final_outputs

# Ініціалізація початкових значень вагових коефіцієнтів
def initialize_weights(rows, cols):
    weights = []
    for _ in range(rows * cols):
        weights.append(np.random.randn())
    return np.array(weights).reshape(rows, cols)

def calculate_loss(output, target):
    squared_error = np.square(output - target)
    loss = np.sum(squared_error) / len(target)
    return loss


def backpropagation(x, y, weights1, weights2, learning_rate):
    # Прихований шар
    hidden_inputs = x.dot(weights1)
    hidden_outputs = sigmoid(hidden_inputs)

    # Вихідний шар
    final_inputs = hidden_outputs.dot(weights2)
    final_outputs = sigmoid(final_inputs)

    output_errors = final_outputs - y
    hidden_errors = np.multiply((weights2.dot((output_errors.transpose()))).transpose(),
                                (np.multiply(hidden_outputs, 1 - hidden_outputs)))

    # Градієнт для weights1 та weights2
    weights1_gradients = x.transpose().dot(hidden_errors)
    weights2_gradients = hidden_outputs.transpose().dot(output_errors)

    # Оновлення параметрів з контролем помилки learning_rate
    weights1 -= learning_rate * weights1_gradients
    weights2 -= learning_rate * weights2_gradients

    return weights1, weights2

def train_network(x, y, weights1, weights2, learning_rate=0.01, num_epochs=10):
    def update_weights(inputs, targets, w1, w2, lr):
        output = forward_propagation(inputs, w1, w2)
        loss = calculate_loss(output, targets)
        updated_w1, updated_w2 = backpropagation(inputs, targets, w1, w2, lr)
        return loss, updated_w1, updated_w2

    def train_epoch(epoch, data, labels, w1, w2, lr):
        epoch_loss, updated_w1, updated_w2 = zip(*[update_weights(x, y, w1, w2, lr) for x, y in zip(data, labels)])
        avg_loss = sum(epoch_loss) / len(data)
        accuracy = (1 - avg_loss) * 100
        print(f"Епоха: {epoch + 1}, Точність: {accuracy:.2f}%")
        return accuracy, avg_loss, updated_w1[-1], updated_w2[-1]

    accuracies, losses, trained_weights1, trained_weights2 = zip(*[train_epoch(epoch, x, y, weights1, weights2, learning_rate) for epoch in range(num_epochs)])
    return accuracies, losses, trained_weights1[-1], trained_weights2[-1]

def predict_symbol(x, weights1, weights2):

    def get_predicted_class(output):
        return max(range(len(output[0])), key=lambda i: output[0][i])

    def get_symbol(predicted_class):
        symbol_mapping = {
            0: "+",
            1: "#",
            2: "&",
            3: "0",
            4: "1",
            5: "C",
            6: "3"
        }
        return symbol_mapping[predicted_class]


    output = forward_propagation(x, weights1, weights2)
    predicted_class = get_predicted_class(output)
    symbol = get_symbol(predicted_class)

    print(f"Зображення символу {symbol}.\n")
    plt.imshow(x.reshape(8, 6), cmap='binary')
    plt.show()

    return


if __name__ == '__main__':
    # Вхідні дані
    input_data = create_input_data()
    output_data = create_output_data()
    print('Масив DataSet: навчальна пара для навчання з учителем')
    print('Вхідні дані:', input_data, '\n')
    print('Вихідні дані:', output_data, '\n')

    layer_sizes = [(48, 16), (16, 7)]
    weights = [initialize_weights(*size) for size in layer_sizes]

    print('Навчання мережі')
    accuracies, losses, *trained_weights = train_network(input_data, output_data, *weights, 0.1, 70)

    training_metrics = [
        ('Точність', accuracies),
        ('Втрати', losses)
    ]
    for metric, data in training_metrics:
        plt.figure()
        plt.plot(data)
        plt.ylabel(metric)
        plt.xlabel("Епохи")
        plt.show()

    symbols = ["+", "#", "&", "0", "1", "C", "3"]
    for i, symbol in enumerate(symbols):
        print(f'Вхідний символ: "{symbol}"')
        print('Ідентифіковано:')
        predict_symbol(input_data[i], *trained_weights)
