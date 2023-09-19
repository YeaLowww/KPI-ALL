import pandas as pd
import requests
import matplotlib.pyplot as plt
import yfinance as yf

# Завантаження даних захворювань на Covid в Україні та Польщі з даних WHO
url_ukraine = "https://covid19.who.int/WHO-COVID-19-global-data.csv"
url_poland = "https://covid19.who.int/WHO-COVID-19-global-data.csv"

data_ukraine = pd.read_csv(url_ukraine, usecols=["Date_reported", "Country_code", "New_cases"], 
                           index_col="Date_reported", parse_dates=True, 
                           squeeze=True, dayfirst=True).loc["2020-03-01":"2023-04-21"].query('Country_code=="UA"')
data_poland = pd.read_csv(url_poland, usecols=["Date_reported", "Country_code", "New_cases"], 
                          index_col="Date_reported", parse_dates=True, 
                          squeeze=True, dayfirst=True).loc["2020-03-01":"2023-04-21"].query('Country_code=="PL"')

# Відображення графіка кількості нових випадків захворювання на Covid в Україні
plt.plot(data_ukraine["New_cases"].values.tolist())
plt.title("Кількість нових випадків захворювання на Covid в Україні")
plt.xlabel("Дата")
plt.ylabel("Кількість випадків")
plt.show()

# Відображення графіка кількості нових випадків захворювання на Covid в Польщі
plt.plot(data_poland["New_cases"].values.tolist())
plt.title("Кількість нових випадків захворювання на Covid в Польщі")
plt.xlabel("Дата")
plt.ylabel("Кількість випадків")
plt.show()

###############################################################################
import tkinter as tk
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from matplotlib.figure import Figure

# Створення вікна Tkinter
root = tk.Tk()
# Завантаження даних за останні 3 роки для курсу USD/UAH зі складу Yahoo Finance
data = yf.download('USDUAH=X', start='2018-04-21', end='2021-04-21', interval='1d')

# Виведення перших  рядків даних
print(data.head())

# Створення фігури matplotlib
fig = Figure(figsize=(6, 4), dpi=100)
ax = fig.add_subplot(111)
ax.plot(data['Close'])

# Створення віджету Canvas Tkinter
canvas = FigureCanvasTkAgg(fig, master=root)
canvas.draw()
canvas.get_tk_widget().pack(side=tk.TOP, fill=tk.BOTH, expand=1)

# Запуск основного циклу Tkinter
root.mainloop()

