package com.example.praktika.ui.onboard

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.praktika.R
import kotlinx.coroutines.launch

// Данные для onboard страниц
data class OnboardPage(
    val title: String,
    val description: String,
    val imageRes: Int
)

val onboardPages = listOf(
    OnboardPage(
        title = "ДОБРО ПОЖАЛОВАТЬ",
        description = "Умная, исполнительная и модная\nколлекция Изумите сейчас",
        imageRes = R.drawable.onboard_1
    ),
    OnboardPage(
        title = "Начнем путешествие",
        description = "Умная, исполнительная и модная\nколлекция Изумите сейчас",
        imageRes = R.drawable.onboard_2
    ),
    OnboardPage(
        title = "У Вас Есть Сила, Чтобы",
        description = "В вашей комнате много красивых и\nпривлекательных растений",
        imageRes = R.drawable.onboard_3
    )
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun OnboardScreen(
    onGetStarted: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { onboardPages.size })
    val coroutineScope = rememberCoroutineScope()

    // Цвета из макета
    val backgroundColor = Color(0xFF48B2E7) // Светло-синий фон
    val buttonColor = Color.White // Белые кнопки
    val buttonTextColor = Color.DarkGray // Темно-синий текст на кнопке
    val indicatorActiveColor = Color.White // Темно-синий для активной точки
    val indicatorInactiveColor = Color.White // Белый для неактивных точек
    val textColor = Color(0xFFFFFFFF) // Темно-синий заголовок
    val descriptionColor = Color.DarkGray // Серый для описания

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 24.dp)
    ) {



        Spacer(modifier = Modifier.height(40.dp))

        // HorizontalPager для свайпа
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val currentPage = onboardPages[page]

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Изображение
                Image(
                    painter = painterResource(id = currentPage.imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(375.dp, 302.dp)
                        .padding(20.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Заголовок
                Text(
                    text = currentPage.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Описание
                Text(
                    text = currentPage.description,
                    fontSize = 14.sp,
                    color = descriptionColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(onboardPages.size) { index ->
                Box(
                    modifier = Modifier
                        .size(
                            width = if (pagerState.currentPage == index) 70.dp else 30.dp,
                            height = 10.dp
                        )
                        .padding(horizontal = 8.dp)
                        .background(
                            color = if (pagerState.currentPage == index)
                                indicatorActiveColor else indicatorInactiveColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))

        // Кнопка
        Button(
            onClick = {
                if (pagerState.currentPage == onboardPages.size - 1) {
                    onGetStarted()
                } else {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = when (pagerState.currentPage) {
                    0 -> "Начать"
                    1 -> "Далее"
                    else -> "Далее"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = buttonTextColor
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        /* Кнопка "Пропустить"
        if (pagerState.currentPage != onboardPages.size - 1) {
            Text(
                text = "Пропустить",
                fontSize = 16.sp,
                color = textColor,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { onGetStarted() }
                    .padding(bottom = 20.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(36.dp))
        }*/
    }
}