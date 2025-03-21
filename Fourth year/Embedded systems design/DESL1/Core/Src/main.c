#include "stm32c0xx.h"

void SystemClock_Config(void);
void GPIO_Init(void);

int main(void)
{
    // Ініціалізація системного годинника
    SystemClock_Config();
    
    // Ініціалізація GPIO
    GPIO_Init();
    
    // Основний цикл
    while (1)
    {
        // Включити світлодіод (встановлюємо високий рівень на пін PA13)
        GPIOA->ODR |= (1 << 13); // Встановлюємо біт 13 на 1 для включення світлодіода
        
        // Затримка
        for (volatile int i = 0; i < 100000; i++);
        
        // Вимкнути світлодіод (встановлюємо низький рівень на пін PA13)
        GPIOA->ODR &= ~(1 << 13); // Встановлюємо біт 13 на 0 для вимкнення світлодіода
        
        // Затримка
        for (volatile int i = 0; i < 100000; i++);
    }
}

void SystemClock_Config(void)
{
    // Налаштування системного годинника (для простоти використовуємо стандартне налаштування)
}

void GPIO_Init(void)
{
    // Увімкнути тактуючу лінію для порту A
    RCC->IOPENR |= RCC_IOPENR_GPIOAEN;
    
    // Налаштування піну PA13 як вихід
    GPIOA->MODER &= ~GPIO_MODER_MODE13;    // Очистити біт
    GPIOA->MODER |= GPIO_MODER_MODE13_0;   // Налаштувати як вихід
    GPIOA->OTYPER &= ~GPIO_OTYPER_OT13;    // Налаштувати як вивід push-pull
    GPIOA->OSPEEDR &= ~GPIO_OSPEEDR_OSPEED13;  // Налаштувати на низьку швидкість
    GPIOA->PUPDR &= ~GPIO_PUPDR_PUPD13;   // Вимкнути підтягувачі
}
