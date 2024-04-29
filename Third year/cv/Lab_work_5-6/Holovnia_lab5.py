import cv2
import numpy as np
from matplotlib import pyplot as plt

# зчитування та відображення зображення
FileIm = "1.png"
img = cv2.imread(FileIm)
img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
plt.imshow(img)
plt.show()

# 1 зображення
img = cv2.GaussianBlur(img, (7, 7), 3)
hsv_img = cv2.cvtColor(img, cv2.COLOR_RGB2HSV)
lower_range = (0, 0, 0)
upper_range = (180, 255, 50)

mask = cv2.inRange(hsv_img, lower_range, upper_range)

kernel = np.ones((5, 5), np.uint8)
mask = cv2.morphologyEx(mask, cv2.MORPH_CLOSE, kernel)

plt.imshow(mask)
plt.show()

img[mask > 0] = (255, 255, 0)
res = img.shape
water = np.sum(mask == 255)
print("Відсоток води : " + str(water / (res[0] * res[1]) * 100))


# 2 зображення
FileIm = "2.png"
img = cv2.imread(FileIm)
img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
plt.imshow(img)
plt.show()

img = cv2.GaussianBlur(img, (7, 7), 3)
hsv_img = cv2.cvtColor(img, cv2.COLOR_RGB2HSV)
lower_range = (70, 0, 0)
upper_range = (180, 255, 100)

mask = cv2.inRange(hsv_img, lower_range, upper_range)

kernel = np.ones((5, 5), np.uint8)
mask = cv2.morphologyEx(mask, cv2.MORPH_CLOSE, kernel)

plt.imshow(mask)
plt.show()

img[mask > 0] = (255, 255, 0)
res = img.shape
water = np.sum(mask == 255)
print("Відсоток води : " + str(water / (res[0] * res[1]) * 100))

