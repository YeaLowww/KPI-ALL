# Importing libraries
import pandas as pd
import numpy as np
import seaborn as sns
import matplotlib.pyplot as plt
from sklearn.cluster import AgglomerativeClustering
from sklearn.preprocessing import StandardScaler
from scipy.cluster.hierarchy import linkage
from scipy.cluster.hierarchy import dendrogram
from sklearn.metrics import accuracy_score
from sklearn.metrics import silhouette_score
from sklearn.metrics import adjusted_rand_score


# Load the data
califhousing_set = pd.read_csv('housing.csv')
califhousing_set
califhousing_set.dropna(inplace=True)

# Build a DataFrame from columns "total_rooms" and "median_house_value",
x_std = califhousing_set[['total_rooms', 'median_house_value']].values
x_std

n_clusters = 4
model = AgglomerativeClustering(n_clusters=n_clusters, linkage='ward')
model.fit(x_std)


# Get true labels
labels = model.labels_
y = califhousing_set['median_house_value'].apply(lambda x: 0 if x < 200000 else 1 if x < 350000 else 2).values
# Calculate accuracy
accuracy = accuracy_score(y, labels)
# Print result
print(f"Accuracy: {accuracy}")
# Обчислення Silhouette score
score = silhouette_score(x_std, labels)
print("Silhouette score:", score)
ari = adjusted_rand_score(y, labels)
print("Adjusted Rand Index:", ari)



# Plot the clusters
plt.scatter(x_std[model.labels_==0, 0], x_std[model.labels_==0, 1], s=100, c = 'red', label='Cluster 1')
plt.scatter(x_std[model.labels_==1, 0], x_std[model.labels_==1, 1], s=100, c = 'blue', label='Cluster 2')
plt.scatter(x_std[model.labels_==2, 0], x_std[model.labels_==2, 1], s=100, c = 'green', label='Cluster 3')
plt.scatter(x_std[model.labels_==3, 0], x_std[model.labels_==3, 1], s=100, c = 'cyan', label='Cluster 4')
plt.title('California housing clusters')
plt.xlabel('Total rooms (area)')
plt.ylabel('Median house value')
plt.legend()
plt.show()


# Build a DataFrame from columns "median_income" and "median_house_value", which we will use as features
x_std = califhousing_set[['median_income', 'median_house_value']].values
x_std

n_clusters = 4
model = AgglomerativeClustering(n_clusters=n_clusters, linkage='ward')
model.fit(x_std)
# Plot the clusters
plt.scatter(x_std[model.labels_==0, 0], x_std[model.labels_==0, 1], s=100, c = 'red', label='Cluster 1')
plt.scatter(x_std[model.labels_==1, 0], x_std[model.labels_==1, 1], s=100, c = 'blue', label='Cluster 2')
plt.scatter(x_std[model.labels_==2, 0], x_std[model.labels_==2, 1], s=100, c = 'green', label='Cluster 3')
plt.scatter(x_std[model.labels_==3, 0], x_std[model.labels_==3, 1], s=100, c = 'cyan', label='Cluster 4')
plt.title('California housing clusters')
plt.xlabel('Median income')
plt.ylabel('Median house value')
plt.legend()
plt.show()


n_clusters = 3
model = AgglomerativeClustering(n_clusters=n_clusters, linkage='ward')
model.fit(x_std)
# Plot the clusters
plt.scatter(x_std[model.labels_==0, 0], x_std[model.labels_==0, 1], s=100, c = 'red', label='Cluster 1')
plt.scatter(x_std[model.labels_==1, 0], x_std[model.labels_==1, 1], s=100, c = 'blue', label='Cluster 2')
plt.scatter(x_std[model.labels_==2, 0], x_std[model.labels_==2, 1], s=100, c = 'green', label='Cluster 3')
plt.title('California housing clusters')
plt.xlabel('Median income')
plt.ylabel('Median house value')
plt.legend()
plt.show()

n_clusters = 6
model = AgglomerativeClustering(n_clusters=n_clusters, linkage='ward')
model.fit(x_std)
# Plot the clusters
plt.scatter(x_std[model.labels_==0, 0], x_std[model.labels_==0, 1], s=100, c = 'red', label='Cluster 1')
plt.scatter(x_std[model.labels_==1, 0], x_std[model.labels_==1, 1], s=100, c = 'blue', label='Cluster 2')
plt.scatter(x_std[model.labels_==2, 0], x_std[model.labels_==2, 1], s=100, c = 'green', label='Cluster 3')
plt.scatter(x_std[model.labels_==3, 0], x_std[model.labels_==3, 1], s=100, c = 'cyan', label='Cluster 4')
plt.scatter(x_std[model.labels_==4, 0], x_std[model.labels_==4, 1], s=100, c = 'black', label='Cluster 5')
plt.scatter(x_std[model.labels_==5, 0], x_std[model.labels_==5, 1], s=100, c = 'brown', label='Cluster 6')

plt.title('California housing clusters')
plt.xlabel('Median income')
plt.ylabel('Median house value')
plt.legend()
plt.show()
