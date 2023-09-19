# Libraries importing
import numpy as np 
import pandas as pd 
import matplotlib.pyplot as plt 
import seaborn as sns
from sklearn.cluster import DBSCAN
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import silhouette_score
from sklearn.metrics import accuracy_score
from sklearn.metrics import adjusted_rand_score

# Read a dataset and show it
califhousing_set = pd.read_csv('housing.csv')
califhousing_set

# Read a dataset and show it
califhousing_set = pd.read_csv('housing.csv')
califhousing_set

# Show the top 5 rows of the datased we read
califhousing_set.head()
# Show the last 5 rows of the datased we read
califhousing_set.tail()


# Show the shape of the dataset
califhousing_set.shape

# Get some general information about the dataset
califhousing_set.info()

# Check the dataset for any missing data
califhousing_set.isna().any()

# Remove all rows that cantains missing elements 
califhousing_set.dropna(inplace=True)

# Get general information about the dataset after the removal
califhousing_set.info()

# Check the dataset for any null elements
califhousing_set.isnull().sum()

# Build a DataFrame from columns "total_rooms" and "median_house_value", which we will use as features
x = califhousing_set[['total_rooms', 'median_house_value']].values
x

print('---------------------------------------------------')
# Importing Kmeans
from sklearn.cluster import KMeans
clalist=[]

#inertia_ is a method that divides data points into clusters
for i in range(1,11):
    kmeans = KMeans(n_clusters= i, init='k-means++', random_state=0)
    kmeans.fit(x)
    clalist.append(kmeans.inertia_)
    
plt.plot(range(1,11), clalist)
plt.title('The elbow method')
plt.xlabel('Number of clusters')
plt.ylabel('clalist')
plt.show()

kmeansmodel = KMeans(n_clusters= 4, init='k-means++', random_state=0)
y_kmeans= kmeansmodel.fit_predict(x)

# Get true labels
y = califhousing_set['median_house_value'].apply(lambda x: 0 if x < 200000 else 1 if x < 350000 else 2).values
# Calculate accuracy
accuracy = accuracy_score(y, y_kmeans)
# Print result
print(f"Accuracy: {accuracy}")
score = silhouette_score(x, y_kmeans)
print("Silhouette score:", score)
ari = adjusted_rand_score(y, y_kmeans)
print("Adjusted Rand Index:", ari)










plt.scatter(x[y_kmeans == 0, 0], x[y_kmeans == 0, 1], s = 100, c = 'red', label = 'Cluster 1')
plt.scatter(x[y_kmeans == 1, 0], x[y_kmeans == 1, 1], s = 100, c = 'blue', label = 'Cluster 2')
plt.scatter(x[y_kmeans == 2, 0], x[y_kmeans == 2, 1], s = 100, c = 'green', label = 'Cluster 3')
plt.scatter(x[y_kmeans == 3, 0], x[y_kmeans == 3, 1], s = 100, c = 'cyan', label = 'Cluster 4')
plt.scatter(kmeans.cluster_centers_[0:10, 0], kmeans.cluster_centers_[0:10, 1], s = 300, c = 'yellow', label = 'Centroids')
plt.title('California housing clusters')
plt.xlabel('Total rooms (area)')
plt.ylabel('Median house value')
plt.legend()
plt.show()

# Build a DataFrame from columns "median_income" and "median_house_value", which we will use as features
x = califhousing_set[['median_income', 'median_house_value']].values
x
clalist=[]
for i in range(1,11):
    kmeans = KMeans(n_clusters= i, init='k-means++', random_state=0)
    kmeans.fit(x)
    clalist.append(kmeans.inertia_)   
plt.plot(range(1,11), clalist)
plt.title('The elbow method')
plt.xlabel('Number of clusters')
plt.ylabel('clalist')
plt.show()

kmeansmodel = KMeans(n_clusters= 4, init='k-means++', random_state=0)
y_kmeans= kmeansmodel.fit_predict(x)

plt.scatter(x[y_kmeans == 0, 0], x[y_kmeans == 0, 1], s = 100, c = 'red', label = 'Cluster 1')
plt.scatter(x[y_kmeans == 1, 0], x[y_kmeans == 1, 1], s = 100, c = 'blue', label = 'Cluster 2')
plt.scatter(x[y_kmeans == 2, 0], x[y_kmeans == 2, 1], s = 100, c = 'green', label = 'Cluster 3')
plt.scatter(x[y_kmeans == 3, 0], x[y_kmeans == 3, 1], s = 100, c = 'cyan', label = 'Cluster 4')
plt.scatter(kmeans.cluster_centers_[0:10, 0], kmeans.cluster_centers_[0:10, 1], s = 300, c = 'yellow', label = 'Centroids')
plt.title('California housing clusters')
plt.xlabel('Median income')
plt.ylabel('Median house value')
plt.legend()
plt.show()


kmeansmodel = KMeans(n_clusters= 3, init='k-means++', random_state=0)
y_kmeans= kmeansmodel.fit_predict(x)

plt.scatter(x[y_kmeans == 0, 0], x[y_kmeans == 0, 1], s = 100, c = 'red', label = 'Cluster 1')
plt.scatter(x[y_kmeans == 1, 0], x[y_kmeans == 1, 1], s = 100, c = 'blue', label = 'Cluster 2')
plt.scatter(x[y_kmeans == 2, 0], x[y_kmeans == 2, 1], s = 100, c = 'green', label = 'Cluster 3')
plt.scatter(kmeans.cluster_centers_[0:10, 0], kmeans.cluster_centers_[0:10, 1], s = 300, c = 'yellow', label = 'Centroids')
plt.title('California housing clusters')
plt.xlabel('Median income')
plt.ylabel('Median house value')
plt.legend()
plt.show()

kmeansmodel = KMeans(n_clusters= 6, init='k-means++', random_state=0)
y_kmeans= kmeansmodel.fit_predict(x)

plt.scatter(x[y_kmeans == 0, 0], x[y_kmeans == 0, 1], s = 100, c = 'red', label = 'Cluster 1')
plt.scatter(x[y_kmeans == 1, 0], x[y_kmeans == 1, 1], s = 100, c = 'blue', label = 'Cluster 2')
plt.scatter(x[y_kmeans == 2, 0], x[y_kmeans == 2, 1], s = 100, c = 'green', label = 'Cluster 3')
plt.scatter(x[y_kmeans == 3, 0], x[y_kmeans == 3, 1], s = 100, c = 'cyan', label = 'Cluster 4')
plt.scatter(x[y_kmeans == 4, 0], x[y_kmeans == 4, 1], s = 100, c = 'black', label = 'Cluster 5')
plt.scatter(x[y_kmeans == 5, 0], x[y_kmeans == 5, 1], s = 100, c = 'brown', label = 'Cluster 6')
plt.scatter(kmeans.cluster_centers_[0:10, 0], kmeans.cluster_centers_[0:10, 1], s = 300, c = 'yellow', label = 'Centroids')
plt.title('California housing clusters')
plt.xlabel('Median income')
plt.ylabel('Median house value')
plt.legend()
plt.show()
print('---------------------------------------------------')
print('---------------------------------------------------')
print('---------------------------------------------------')












