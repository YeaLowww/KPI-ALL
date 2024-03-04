from PIL import Image, ImageFilter
import numpy as np

def load_image(file_path):
    return Image.open(file_path).convert('RGB')

def save_image(img, file_path):
    img.save(file_path)

def change_brightness(img, factor):
    img_array = np.array(img, dtype=np.float32)
    img_array *= factor
    img_array = np.clip(img_array, 0, 255).astype(np.uint8)
    return Image.fromarray(img_array)

def add_noise(img, sigma):
    img_array = np.array(img)
    noise = np.random.normal(0, sigma, img_array.shape)
    noisy_img_array = img_array + noise
    noisy_img_array = np.clip(noisy_img_array, 0, 255).astype(np.uint8)
    return Image.fromarray(noisy_img_array)

def gaussian_blur(img, radius):
    return img.filter(ImageFilter.GaussianBlur(radius))

def make_negative(img):
    img_array = np.array(img)
    negative_img_array = 255 - img_array
    return Image.fromarray(negative_img_array)

def main():
    # Load image
    file_path = "file.png"
    img = load_image(file_path)

    # Apply operations
    img = change_brightness(img, 1.5)
    img = add_noise(img, 25)
    img = gaussian_blur(img, 2)
    img = make_negative(img)

    # Save image
    save_path = "saved.jpg"
    save_image(img, save_path)

if __name__ == "__main__":
    main()
