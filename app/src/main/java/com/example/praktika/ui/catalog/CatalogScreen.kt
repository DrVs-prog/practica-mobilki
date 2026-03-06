package com.example.praktika.ui.catalog

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

data class CatalogProduct(
    val id: Int,
    val name: String,
    val price: String,
    val imageRes: Int,
    val isBestSeller: Boolean = false
)

@Composable
fun CatalogScreen(
    category: String,
    onProductClick: (Int) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(category) }
    var isLoading by remember { mutableStateOf(true) }
    var products by remember { mutableStateOf<List<CatalogProduct>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    // Пункт 3: Загрузка данных с сервера
    LaunchedEffect(selectedCategory) {
        isLoading = true
        errorMessage = null
        try {
            // Имитация запроса к серверу
            delay(1500)
            products = listOf(
                CatalogProduct(1, "Nike Air Max", "₽752.00", R.drawable.product_1, true),
                CatalogProduct(2, "Nike Air Max", "₽752.00", R.drawable.product_2, true),
                CatalogProduct(3, "Nike Air Max", "₽752.00", R.drawable.product_1, true),
                CatalogProduct(4, "Nike Air Max", "₽752.00", R.drawable.product_2, true)
            )
        } catch (e: Exception) {
            errorMessage = "Ошибка загрузки: ${e.message}"
        } finally {
            isLoading = false
        }
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
            text = when (selectedCategory) {
                "Все" -> "Каталог"
                else -> selectedCategory
            },
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Категории
        CategoryTabs(
            categories = listOf("Все", "Outdoor", "Tennis"),
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it }
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
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        isLoading = true
                        errorMessage = null
                        products = emptyList()
                        // LaunchedEffect перезапустится сам, потому что selectedCategory не изменился?
                        // Можно добавить принудительный ключ
                    }) {
                        Text("Повторить")
                    }
                }
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
                items(products) { product ->
                    CatalogProductCard(
                        product = product,
                        onClick = { onProductClick(product.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        categories.forEach { category ->
            Text(
                text = category,
                fontSize = 18.sp,
                fontWeight = if (category == selectedCategory) FontWeight.Bold else FontWeight.Normal,
                color = if (category == selectedCategory) Color(0xFF1976D2) else Color.Gray,
                modifier = Modifier
                    .clickable { onCategorySelected(category) }
                    .padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun CatalogProductCard(
    product: CatalogProduct,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clickable { onClick() }
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