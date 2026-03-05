package com.example.praktika.ui.splash


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.example.praktika.R

@Composable
fun SplashScreen(
    onTimeout: () -> Unit
) {
    // Запускаем таймер на 2 секунды
    LaunchedEffect(Unit) {
        delay(5000)
        onTimeout()
    }

    val backgroundColor = Color(0xFF48B2E7) // Темно-синий
    val textColor = Color.White // Белый текст

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Логотип (используем иконку приложения)
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground1),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}