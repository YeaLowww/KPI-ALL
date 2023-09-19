import pandas as pd
import matplotlib.pyplot as plt

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

#print(f"{'Country Name':<40}{'Region':<40}{'GDP per capita':<20}{'Populatiion':<20}{'CO2 emission':<20}{'Area':<20}")
# виведення даних у форматованому вигляді
#for index, row in df.iterrows():
#    print(f"{row['Country Name']:<40}{row['Region']:<40}{row['GDP per capita']:<20}{row['Populatiion']:<20}{row['CO2 emission']:<20}{row['CO2 emission']:<20}")

# побудова діаграм розмаху та гістограм для кожної змінної
for column in df.columns:
    if df[column].dtype in ['float64', 'int64']:
        fig, ax = plt.subplots(1, 2, figsize=(12,6))
        ax[0].boxplot(df[column])
        ax[0].set_title(f"{column} Boxplot")
        ax[0].set_ylabel(column)
        ax[1].hist(df[column], bins=30)
        ax[1].set_title(f"{column} Histogram")
        ax[1].set_xlabel(column)
        plt.show()

# Діаграма розмаху та гістограма для Area
fig, ax = plt.subplots(1, 2, figsize=(12,4))
ax[0].boxplot(df['Area'])
ax[0].set_title("Area Boxplot")
ax[0].set_ylabel("Area")
ax[1].hist(df['Area'], bins=30)
ax[1].set_title("Area Histogram")
ax[1].set_xlabel("Area")
plt.show()


df['Population Density'] = df['Populatiion'] / df['Area']
#print(f"{'Country Name':<40}{'Region':<40}{'GDP per capita':<20}{'Populatiion':<20}{'CO2 emission':<20}{'Area':<20}{'Population Density':<20}")
# виведення даних у форматованому вигляді
#for index, row in df.iterrows():
#    print(f"{row['Country Name']:<40}{row['Region']:<40}{row['GDP per capita']:<20}{row['Populatiion']:<20}{row['CO2 emission']:<20}{row['CO2 emission']:<20}{row['Population Density']:<20}   ")

max_gdp_row = df.loc[df['GDP per capita'].idxmax()]
print('\nmax gdp row:')
print(max_gdp_row)
print('\nmin area row:')
min_row = df.loc[df['Area'].idxmin()]
print(min_row)

mean_area_by_region = df.groupby('Region')['Area'].mean().sort_values(ascending=False)

print(mean_area_by_region)
largest_region = mean_area_by_region.index[0]
print(f"The region with the largest average country area is {largest_region}\n")

max_pd_row = df.loc[df['Population Density'].idxmax()]
print(max_pd_row)


# Групування даних за регіоном
region_groups = df.groupby("Region")

# Створення списку регіонів, для яких середнє та медіанне значення співпадають
matching_regions = []
for region, data in region_groups:
    mean_gdp = data["GDP per capita"].mean()
    median_gdp = data["GDP per capita"].median()
    if mean_gdp == median_gdp:
        matching_regions.append(region)
print('\nСпівпадіння середнього і медіани:')
print(matching_regions)

# Вибір топ 5 країн та останніх 5 країн по GDP per capita
top_gdp = df.sort_values(by='GDP per capita', ascending=False).head(5)
last_gdp = df.sort_values(by='GDP per capita').head(5)

# Вибір топ 5 країн та останніх 5 країн по кількості CO2 викидів
top_co2 = df.sort_values(by='CO2 emission', ascending=False).head(5)
last_co2 = df.sort_values(by='CO2 emission').head(5)

# Виведення результатів
print('Топ 5 країн по GDP per capita:')
print(top_gdp[['Country Name', 'GDP per capita']])

print('\nОстанні 5 країн по GDP per capita:')
print(last_gdp[['Country Name', 'GDP per capita']])

print('\nТоп 5 країн по кількості CO2 викидів:')
print(top_co2[['Country Name', 'CO2 emission']])

print('\nОстанні 5 країн по кількості CO2 викидів:')
print(last_co2[['Country Name', 'CO2 emission']])

