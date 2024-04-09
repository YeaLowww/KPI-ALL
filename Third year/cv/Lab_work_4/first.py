import cv2
import numpy as np

# Завантаження зображення Google Карт
image = cv2.imread('google_map_image.png')

# Фільтрування коричневих та білих об'єктів на зображенні
hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
lower_brown = np.array([10, 50, 50])
upper_brown = np.array([30, 255, 255])
brown_mask = cv2.inRange(hsv, lower_brown, upper_brown)

lower_white = np.array([0, 0, 200])
upper_white = np.array([255, 50, 255])
white_mask = cv2.inRange(hsv, lower_white, upper_white)

mask = cv2.bitwise_or(brown_mask, white_mask)
result = cv2.bitwise_and(image, image, mask=mask)

# Покращення якості зображення 
gray = cv2.cvtColor(result, cv2.COLOR_BGR2GRAY)
equalized = cv2.equalizeHist(gray)
blurred = cv2.medianBlur(equalized, 5)

# Векторизація об'єктів ідентифікації
_, thresh = cv2.threshold(blurred, 127, 255, cv2.THRESH_BINARY)
kernel = np.ones((5, 5), np.uint8)
thresh = cv2.erode(thresh, kernel, iterations=1)
thresh = cv2.dilate(thresh, kernel, iterations=1)
contours, _ = cv2.findContours(thresh, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

# Ідентифікація
for contour in contours:
    # Визначення координат і розмірів квадрата
    x, y, w, h = cv2.boundingRect(contour)
    # Відображення квадрата навколо ідентифікованої бочки
    cv2.rectangle(image, (x, y), (x + w, y + h), (0, 255, 0), 2)

# Відображення результатів
cv2.imshow('Identified Barrels', image)
cv2.waitKey(0)
cv2.destroyAllWindows()
