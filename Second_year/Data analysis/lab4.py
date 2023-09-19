import pandas as pd
import matplotlib.pyplot as plt
from scipy.stats import shapiro
from scipy.stats import ttest_1samp
import seaborn as sns


# зчитування файлу та запис даних у DataFrame
df = pd.read_csv("Data2.csv", delimiter=";")

# Зміна типів даних у стовпцях
df['Country Name'] = df['Country Name'].astype('string')
df['Region'] = df['Region'].astype('string')

df['GDP per capita'] = df['GDP per capita'].str.replace(',', '.').astype(float)
df['GDP per capita'] = df['GDP per capita'].astype('float')

mean_populatiion = df['Populatiion'].mean()
df['Populatiion'] = df['Populatiion'].fillna(mean_populatiion).astype(int)
df['Populatiion'] = df['Populatiion'].astype('int')

df['CO2 emission'] = df['CO2 emission'].str.replace(',', '.').astype(float)
df['CO2 emission'] = df['CO2 emission'].astype('float')

df['Area'] = df['Area'].str.replace(',', '').astype(int)
df['Area'] = df['Area'].astype('int')

# замінюємо від'ємні значення в стовпці Populatiion на середнє значення
mean_populatiion = df['Populatiion'].mean()
df.loc[df['Populatiion'] < 0, 'Populatiion'] = mean_populatiion
# замінюємо від'ємні значення в стовпці Сфзшеф на середнє значення
mean_GDP = df['GDP per capita'].mean()
df.loc[df['GDP per capita'] < 0, 'GDP per capita'] = mean_GDP
# замінюємо від'ємні значення в стовпці CO2 emission на середнє значення
mean_CO2 = df['CO2 emission'].mean()
df.loc[df['CO2 emission'] < 0, 'CO2 emission'] = mean_CO2
# замінюємо від'ємні значення в стовпці Area на середнє значення
mean_area = df['Area'].mean()
df.loc[df['Area'] < 0, 'Area'] = mean_area

# замінюємо NaN значення середнім значенням
mean_GDP = df['GDP per capita'].mean()
df['GDP per capita'] = df['GDP per capita'].fillna(mean_GDP)
# замінюємо NaN значення середнім значенням
mean_CO2 = df['CO2 emission'].mean()
df['CO2 emission'] = df['CO2 emission'].fillna(mean_CO2)
# заміна NaN значень у стовпці "Area" на середнє значення
mean_area = df['Area'].mean()
df['Area'] = df['Area'].fillna(mean_area).astype(int)


# GDP per capita
stat, p = shapiro(df['GDP per capita'])
alpha = 0.05
if p > alpha:
    print("GDP per capita is normally distributed")
else:
    print("GDP per capita is not normally distributed")
    
# Population
stat, p = shapiro(df['Populatiion'])
alpha = 0.05
if p > alpha:
    print("Population is normally distributed")
else:
    print("Population is not normally distributed")
    
# CO2 emission
stat, p = shapiro(df['CO2 emission'])
alpha = 0.05
if p > alpha:
    print("CO2 emission is normally distributed")
else:
    print("CO2 emission is not normally distributed")
    
# Area
stat, p = shapiro(df['Area'])
alpha = 0.05
if p > alpha:
    print("Area is normally distributed")
else:
    print("Area is not normally distributed")

# вибірка
sample = df['GDP per capita']

# перевірка гіпотези про рівність середнього і медіани
t_stat, p_val = ttest_1samp(sample, sample.median())

# виведення результату
if p_val < 0.05:
    print("Різниця між середнім та медіаною значима")
else:
    print("Різниця між середнім та медіаною не є значимою")

# Розділення даних на регіони
regions = df['Region'].unique()

# Для кожного регіону:
for region in regions:
    # Відбір даних для поточного регіону
    data = df[df['Region'] == region]['CO2 emission']

    # Побудова гістограми
    #data.plot.hist(alpha=0.5, bins=30)

    # Перевірка нормальності розподілу
    stat, p = shapiro(data)
    if p > 0.05:
        print(f"{region}: розподіл є нормальним (p-value = {p:.3f})")
    else:
        print(f"{region}: розподіл не є нормальним (p-value = {p:.3f})")

# Обчислення сумарного населення за регіонами
pop_by_region = df.groupby("Region")["Populatiion"].sum()

# Побудова кругової діаграми

plt.pie(pop_by_region, labels=pop_by_region.index, autopct='%1.1f%%')
plt.title("Населення країн за регіонами")
plt.show()




