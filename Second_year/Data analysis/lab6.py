import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score

# Завантаження даних з файлу
data = pd.read_csv('titanic.csv')

# Видалення непотрібних стовпців
data = data.drop(['PassengerId', 'Name', 'Ticket', 'Cabin'], axis=1)

# Обробка пропущених значень
data = data.dropna()

# Перетворення категоріальних змінних в числові
data['Sex'] = data['Sex'].map({'male': 0, 'female': 1})
data['Embarked'] = data['Embarked'].map({'S': 0, 'C': 1, 'Q': 2})

# Розділення даних на навчальний та тестовий набори
X = data.drop('Survived', axis=1)
y = data['Survived']
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)


# Побудова моделі логістичної регресії
lr_model = LogisticRegression()
lr_model.fit(X_train, y_train)

# Перевірка точності на тестовому наборі
y_pred_lr = lr_model.predict(X_test)
accuracy_lr = accuracy_score(y_test, y_pred_lr)

print('Accuracy of Logistic Regression:', accuracy_lr)


# Побудова моделі дерева рішень
dt_model = DecisionTreeClassifier()
dt_model.fit(X_train, y_train)

# Перевірка точності на тестовому наборі
y_pred_dt = dt_model.predict(X_test)
accuracy_dt = accuracy_score(y_test, y_pred_dt)

print('Accuracy of Decision Tree:', accuracy_dt)

# Побудова моделі випадкового лісу
rf_model = RandomForestClassifier(n_estimators=100)
rf_model.fit(X_train, y_train)

# Перевірка точності на тестовому наборі
y_pred_rf = rf_model.predict(X_test)
accuracy_rf = accuracy_score(y_test, y_pred_rf)

print('Accuracy of Random Forest:', accuracy_rf)

# Порівняння результатів
if accuracy_lr > accuracy_dt and accuracy_lr > accuracy_rf:
    print('Logistic Regression is the best model')
elif accuracy_dt > accuracy_lr and accuracy_dt > accuracy_rf:
    print('Decision Tree is the best model')
else:
    print('Random Forest is the best model')


