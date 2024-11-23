import datetime
import pandas as pd
import requests
from bs4 import BeautifulSoup

import requests.exceptions

def check_product_on_site(product_name):
    try:
        search_url = f"https://bolf.ua/search.php?text={product_name.replace(' ', '+')}"
        print()
        print("Доступно за посиланням:" + search_url)
        response = requests.get(search_url)

        soup = BeautifulSoup(response.text, 'html.parser')
        product_listing = soup.find('section', class_='search')

        product = soup.find('div', class_='product')
        if not product:
            return 'error', f"Товар '{product_name}' наразі не існує на Bolf | Status: НЕ можна придбати"
        
        model = product.find('a', class_='product__name').text
        result = f"Товар '{model}' наразі існує "
        availability = product.find('div', class_='product__quick_cart_form')

        if availability:
            status = 'Немає'
            result += '| Status: НЕ можна придбати'
        else:
            status = 'Є'
            result += '| Status: Можна придбати'

        return status, result

    except Exception as e:
        return 'error', f"An error occurred: {e}"


def check_products(products):
    statuses, times = [], []

    for product in products:
        status, message = check_product_on_site(product)
        current_time = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        statuses.append(status if status != 'error' else '')
        times.append(current_time)
        print(current_time+message)

    return statuses, times


if __name__ == '__main__':
    products_df = pd.read_excel('products2.xlsx')
    products_df['status'], products_df['time'] = check_products(products_df['model'])
    products_df.to_excel('products_availability2.xlsx', index=False)
