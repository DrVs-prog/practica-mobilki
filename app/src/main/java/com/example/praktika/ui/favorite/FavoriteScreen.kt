package com.example.praktika.ui.favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.praktika.R
import kotlinx.coroutines.delay

data class FavoriteProduct(
    val id: Int,
    val name: String,
    val price: String,
    val imageRes: Int,
    val isBestSeller: Boolean = false
)

@Composable
fun FavoriteScreen() {
    var isLoading by remember { mutableStateOf(true) }
    var favorites by remember { mutableStateOf<List<FavoriteProduct>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var retryTrigger by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Пункт 5: Загрузка данных с сервера
    LaunchedEffect(retryTrigger) {
        isLoading = true
        errorMessage = null
        try {
            delay(1500)
            favorites = listOf(
                FavoriteProduct(1, "Nike Air Max", "₽752.00", R.drawable.product_1, true),
                FavoriteProduct(2, "Nike Air Max", "₽752.00", R.drawable.product_2, true)
            )
        } catch (e: Exception) {
            errorMessage = "Ошибка загрузки: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    // Пункт 6: Удаление из избранного
    fun removeFromFavorites(productId: Int) {
        favorites = favorites.filter { it.id != productId }
        // TODO: Отправить на сервер
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Время
        Text(
            text = "9:41",
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // Заголовок
        Text(
            text = "Избранное",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Контент
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Button(onClick = { retryTrigger = !retryTrigger }) {
                        Text("Повторить")
                    }
                }
            }
        } else if (favorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "В избранном пока пусто",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(favorites) { product ->
                    FavoriteProductCard(
                        product = product,
                        onRemoveClick = { removeFromFavorites(product.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteProductCard(
    product: FavoriteProduct,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color.White)
            ) {
                if (product.isBestSeller) {
                    Text(
                        text = "BEST SELLER",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .background(Color.White, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )

                // Кнопка удаления
                Icon(
                    painter = painterResource(id = R.drawable.favorite_filled),
                    contentDescription = "Удалить",
                    tint = Color.Red,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp)
                        .clickable { onRemoveClick() }
                )
            }

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = product.price,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}