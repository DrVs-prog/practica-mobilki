package com.example.praktika.ui.home

import android.graphics.Bitmap
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.praktika.R
import com.example.praktika.ui.cart.CartScreen
import com.example.praktika.ui.favorite.FavoriteScreen
import com.example.praktika.ui.profile.ProfileScreen
import com.example.praktika.ui.profile.UserProfile

@Composable
fun HomeScreen(
    navController: NavController
) {
    var selectedTab by remember { mutableStateOf(0) }

    var userProfile by remember { mutableStateOf(UserProfile()) }
    var photoBitmap by remember { mutableStateOf<Bitmap?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> HomeContent(
                    onCategoryClick = { category ->
                        // Переход в каталог с выбранной категорией
                        navController.navigate("catalog/$category")
                    }
                )
                1 -> FavoriteScreen()
                2 -> CartScreen()
                3 -> ProfileScreen(
                    userProfile = userProfile,
                    photoBitmap = photoBitmap,
                    onProfileUpdate = { newProfile, newPhoto ->
                        userProfile = newProfile
                        photoBitmap = newPhoto
                    }
                )
            }
        }

        BottomNavigationBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )
    }
}

@Composable
fun HomeContent(onCategoryClick: (String) -> Unit) {
    var selectedCategory by remember { mutableStateOf("Все") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        HomeHeader()

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Категории",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            CategoryRow(
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                    onCategoryClick(category) // 👈 открываем каталог
                }
            )

            SectionHeader(title = "Популярное")

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.heightIn(max = 500.dp)
            ) {
                items(HomeData.popularProducts) { product ->
                    ProductCard(product = product)
                }
            }

            SectionHeader(title = "Акции")

            Image(
                painter = painterResource(id = R.drawable.promo),
                contentDescription = "Акция Summer Sale",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 20.dp)
            )
        }
    }
}

@Composable
fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Главная",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Icon(
            painter = painterResource(id = R.drawable.marker),
            contentDescription = "Поиск",
            tint = Color.Black
        )
    }
}

@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Все",
            fontSize = 14.sp,
            color = Color(0xFF1976D2),
            modifier = Modifier.clickable { }
        )
    }
}

@Composable
fun CategoryRow(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(70.dp),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .padding(horizontal = 40.dp)
    ) {
        HomeData.categories.forEach { category ->
            Text(
                text = category.name,
                fontSize = 16.sp,
                fontWeight = if (selectedCategory == category.name) FontWeight.Bold else FontWeight.Normal,
                color = if (selectedCategory == category.name) Color(0xFF1976D2) else Color.Black,
                modifier = Modifier.clickable { onCategorySelected(category.name) }
            )
        }
    }
}

@Composable
fun ProductCard(product: Product) {
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

@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(80.dp)
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Главная",
                    modifier = Modifier.size(28.dp)
                )
            },
            label = {
                Text(
                    text = "Главная",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            alwaysShowLabel = true,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1976D2),
                selectedTextColor = Color(0xFF1976D2),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.favorite),
                    contentDescription = "Избранное",
                    modifier = Modifier.size(28.dp)
                )
            },
            label = {
                Text(
                    text = "Избранное",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            alwaysShowLabel = true,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1976D2),
                selectedTextColor = Color(0xFF1976D2),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.orders),
                    contentDescription = "Корзина",
                    modifier = Modifier.size(28.dp)
                )
            },
            label = {
                Text(
                    text = "Корзина",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            alwaysShowLabel = true,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1976D2),
                selectedTextColor = Color(0xFF1976D2),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Профиль",
                    modifier = Modifier.size(28.dp)
                )
            },
            label = {
                Text(
                    text = "Профиль",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            alwaysShowLabel = true,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1976D2),
                selectedTextColor = Color(0xFF1976D2),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
    }
}