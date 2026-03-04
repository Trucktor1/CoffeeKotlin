package com.example
import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class CoffeeShop {

    suspend fun prepareCoffee(type: String = "Американо", orderNumber: Int) {
        val preparationTime = when (type) {
            "Эспрессо" -> (500L..1000L).random()
            "Американо" -> (1000L..2000L).random()
            "Капучино" -> (2000L..3500L).random()
            "Латте" -> (2500L..4000L).random()
            else -> (1550L..3050L).random()
        }
        println("Заказ $orderNumber: Начинаем готовить $type (${preparationTime}мс)")
        delay(preparationTime)
        println("Заказ $orderNumber: $type готов!")
    }

    suspend fun prepareSandwich(type: String = "Клаб-сэндвич", orderNumber: Int) {
        val preparationTime = when (type) {
            "Простой сэндвич" -> (1000L..2000L).random()
            "Клаб-сэндвич" -> (2000L..3500L).random()
            "Тост с авокадо" -> (1500L..2500L).random()
            "Бургер" -> (3200L..5900L).random()
            else -> (2100L..4500L).random()
        }
        println("Заказ $orderNumber: Начинаем готовить $type (${preparationTime}мс)")
        delay(preparationTime)
        println("Заказ $orderNumber: $type готов!")
    }

    suspend fun prepareJuice(type: String = "Апельсиновый сок", orderNumber: Int) {
        val preparationTime = (500L..1550L).random()
        println("Заказ $orderNumber: Начинаем готовить $type (${preparationTime}мс)")
        delay(preparationTime)
        println("Заказ $orderNumber: $type готов!")
    }

    suspend fun takeRandomOrder(orderNumber: Int) {
        println("Заказ $orderNumber принят")

        val items = mutableListOf<Job>()

        coroutineScope {
            val coffeeType = listOf("Эспрессо", "Американо", "Капучино", "Латте").random()
            items.add(launch { prepareCoffee(coffeeType, orderNumber) })

            if (Random.nextBoolean()) {
                val sandwichType = listOf("Простой сэндвич", "Клаб-сэндвич", "Тост с авокадо").random()
                items.add(launch { prepareSandwich(sandwichType, orderNumber) })
            }

            if (Random.nextBoolean()) {
                val juiceType = listOf("Апельсиновый сок", "Яблочный сок", "Морковный фреш").random()
                items.add(launch { prepareJuice(juiceType, orderNumber) })
            }

            items.forEach { it.join() }
        }

        println("Заказ $orderNumber полностью готов! (${items.size} позиций)")
        println()
    }
}

fun main() = runBlocking {
    val coffeeShop = CoffeeShop()

    println("Добро пожаловать в расширенную кофейню!")
    println("Сегодня у нас специальное меню с разными позициями\n")

    val totalOrders = 5

    val totalTime = measureTimeMillis {
        coroutineScope {
            repeat(totalOrders) { orderIndex ->
                val orderNumber = orderIndex + 1
                launch {
                    try {
                        coffeeShop.takeRandomOrder(orderNumber)
                    } catch (e: Exception) {
                        println("Ошибка в заказе $orderNumber: ${e.message}")
                    }
                }
                delay(Random.nextLong(300L, 800L)) // Разная задержка между заказами
            }
        }
    }

    println("Итоговая статистика:")
    println("   Всего заказов: $totalOrders")
    println("   Общее время работы: ${totalTime}мс")
    println("   Среднее время на заказ: ${totalTime / totalOrders}мс")
    println("   Максимальное параллельных заказов: ${totalOrders}")
    println("\nКофейня закрывается! Хорошего дня!")
}