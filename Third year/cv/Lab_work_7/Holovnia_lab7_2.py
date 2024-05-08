import cv2
import numpy as np

def extract_features(image_path):
    # Завантаження зображення
    img = cv2.imread(image_path, cv2.IMREAD_GRAYSCALE)
    
    sift = cv2.SIFT_create()

    keypoints, descriptors = sift.detectAndCompute(img, None)
    
    return descriptors

# Функція для порівняння особливостей
def compare_features(features1, features2):
    # Порівняння особливостей
    bf = cv2.BFMatcher()
    matches = bf.knnMatch(features1, features2, k=2)
    
    # Визначення "хороших" збігів
    good_matches = []
    for m, n in matches:
        if m.distance < 0.75 * n.distance:
            good_matches.append([m])
    
    # Розрахунок відношення кількості "хороших" збігів до загальної кількості збігів
    similarity_ratio = len(good_matches) / len(matches)
    
    return similarity_ratio

# Зразок відомих відбитків пальців (зображення та їх особливості)
known_fingerprints = {
    'user1': extract_features('fingerprint1.png'),
    'user2': extract_features('fingerprint2.png'),
    #'user22': extract_features('fingerprint3.png'),
}

# Зображення відбитка пальця, яке потрібно ідентифікувати
unknown_fingerprint = extract_features('fingerprintTest.png')

# Порівняння з відомими відбитками пальців
for user, features in known_fingerprints.items():
    similarity_ratio = compare_features(features, unknown_fingerprint)
    if similarity_ratio > 0.1:  # Задайте поріг для визначення співпадіння
        print(f"Користувач ідентифікований як {user}")
        break
else:
    print("Користувач не ідентифікований")
