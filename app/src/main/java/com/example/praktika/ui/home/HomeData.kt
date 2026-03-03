package com.example.praktika.ui.home


import androidx.annotation.DrawableRes
import com.example.praktika.R

data class Category(
    val name: String,
    val isSelected: Boolean = false
)

data class Product(
    val name: String,
    val price: String,
    @DrawableRes val imageRes: Int,
    val isBestSeller: Boolean = false
)

data class Promotion(
    val title: String,
    val description: String,
    val tag: String? = null,
    @DrawableRes val imageRes: Int
)

object HomeData {
    val categories = listOf(
        Category("Все", isSelected = true),
        Category("Outdoor"),
        Category("Tennis")
    )

    val popularProducts = listOf(
        Product(
            name = "Nike Air Max",
            price = "P752.00",
            imageRes = R.drawable.product_1, // Добавьте картинки
            isBestSeller = true
        ),
        Product(
            name = "Nike Air Max",
            price = "P752.00",
            imageRes = R.drawable.product_2,
            isBestSeller = true
        )
    )

    val promotions = listOf(
        Promotion(
            title = "Summer Sale",
            description = "15% OFF",
            tag = "NEW!",
            imageRes = R.drawable.promo_1
        )
    )
}