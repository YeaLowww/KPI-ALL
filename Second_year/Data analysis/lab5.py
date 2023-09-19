import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
import seaborn as sns
import matplotlib.pyplot as plt
from sklearn.preprocessing import PolynomialFeatures
from sklearn.pipeline import make_pipeline
from sklearn.metrics import r2_score
from sklearn.metrics import mean_squared_error

# Зчитуємо CSV-файл
df = pd.read_csv('winequality-red.csv',sep=',',encoding='utf-8')

#print(df.columns)
#print(df.head(5))
#print(df.isnull().sum())

sns.countplot(x='quality', data=df)
plt.show()

# підготовка даних для лінійної регресії
X = df.iloc[:, 11].values.reshape(-1, 1) # quality
y = df.iloc[:, 10].values.reshape(-1, 1) # alcohol

# розділення даних на навчальну та тестову вибірки
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=0)

# побудова та навчання моделі лінійної регресії
regressor = LinearRegression()
regressor.fit(X_train, y_train)
# прогнозування значень на тестовій вибірці
y_pred = regressor.predict(X_test)
# оцінка якості моделі
print('Linear Regression R-squared:', r2_score(y_test, y_pred))


# підготовка даних для багатомірної регресії
X = df.iloc[:, :-1].values # всі ознаки, крім quality
y = df.iloc[:, 11].values.reshape(-1, 1) # quality
# розділення даних на навчальну та тестову вибірки
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=0)
# побудова та навчання моделі багатомірної регресії
regressor = LinearRegression()
regressor.fit(X_train, y_train)
# прогнозування значень на тестовій вибірці
y_pred = regressor.predict(X_test)
# оцінка якості моделі
print('Multivariate Linear Regression R-squared:', r2_score(y_test, y_pred))


# підготовка даних для поліноміальної регресії
X = df.iloc[:, 10].values.reshape(-1, 1) # alcohol
y = df.iloc[:, 11].values.reshape(-1, 1) # quality
# розділення даних на навчальну та тестову вибірки
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=0)
# побудова та навчання моделі поліноміальної регресії
model = make_pipeline(PolynomialFeatures(4), LinearRegression())
model.fit(X_train, y_train)
# прогнозування значень на тестовій вибірці
y_pred = model.predict(X_test)
# оцінка якості моделі
print('Polynomial Regression R-squared:', r2_score(y_test, y_pred))

###########################################################################

#df1 = pd.read_csv('Data4.csv',delimiter=";",encoding='ISO-8859-1',on_bad_lines='skip')
#df2 = pd.read_csv('Data4t.csv',delimiter=";",encoding='ISO-8859-1',on_bad_lines='skip')
#print(df1.describe())
#print(df1.head(5))

#corr_matrix = df1.corr(numeric_only=True)
#sns.heatmap(corr_matrix, annot=True, cmap='coolwarm')










