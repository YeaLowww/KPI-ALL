import cv2
import numpy as np


# Завантаження зображення
image = cv2.imread('file.jpg')

# Застосування фільтра для виявлення контурів (Canny)
edges = cv2.Canny(image, 100, 200)

# Знайдення контурів
contours, _ = cv2.findContours(edges, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

# Знаходження контуру за max площею 
max_contour = max(contours, key=cv2.contourArea)

# Намалювати контур
cv2.drawContours(image, [max_contour], 0, (0, 255, 0), 2)

# Відобразити результат
cv2.imshow('Contour', image)
cv2.waitKey(0)
cv2.destroyAllWindows()
