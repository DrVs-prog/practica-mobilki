package com.example.praktika.ui.auth

import android.app.AlertDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationScreen(
    onBackClick: () -> Unit,
    onSuccess: () -> Unit
) {
    var code by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var timerSeconds by remember { mutableStateOf(60) }
    var isTimerRunning by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isTimerRunning) {
        if (isTimerRunning) {
            while (timerSeconds > 0) {
                delay(1000)
                timerSeconds--
            }
            isTimerRunning = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заголовок
        Text(
            text = "OTP Проверка",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Подзаголовок
        Text(
            text = "Пожалуйста, Проверьте Свою Электронную Почту,\nЧтобы Увидеть Код Подтверждения",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            lineHeight = 20.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Заголовок ОТР Код
        Text(
            text = "ОТР Код",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textAlign = TextAlign.Center
        )

        // Поле для OTP кода (исправлено - без дублирования)
        OtpInputField(
            code = code,
            onCodeChange = {
                if (it.length <= 6) {
                    code = it
                    isError = false
                }
            },
            isError = isError,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Таймер
        if (timerSeconds > 0) {
            Text(
                text = "Отправить код повторно через ${timerSeconds} сек",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        } else {
            Text(
                text = "Отправить код повторно",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable(enabled = !isLoading) {
                    timerSeconds = 60
                    isTimerRunning = true
                    Toast.makeText(context, "Код отправлен повторно", Toast.LENGTH_SHORT).show()
                }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Кнопка подтверждения
        Button(
            onClick = {
                if (code.length == 6) {
                    isLoading = true
                    coroutineScope.launch {
                        verifyCode(code) { isValid ->
                            isLoading = false
                            if (isValid) {
                                onSuccess()
                            } else {
                                isError = true
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = code.length == 6 && !isLoading,
            shape = RoundedCornerShape(8.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Подтвердить",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка назад
        Text(
            text = "Назад",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable { onBackClick() }
                .padding(vertical = 8.dp)
        )
    }
}

// ИСПРАВЛЕНО: убрано дублирование цифр
@Composable
fun OtpInputField(
    code: String,
    onCodeChange: (String) -> Unit,
    isError: Boolean,
    enabled: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 0..5) {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(54.dp)
                    .padding(horizontal = 4.dp)
            ) {
                BasicTextField(
                    value = if (i < code.length) code[i].toString() else "",
                    onValueChange = { newValue ->
                        // Берем только последний символ если ввели несколько
                        val digit = newValue.takeLast(1).filter { it.isDigit() }

                        when {
                            digit.isNotEmpty() -> {
                                // Ввод цифры
                                val newCode = code.toMutableList()
                                if (i < code.length) {
                                    newCode[i] = digit[0]
                                } else {
                                    newCode.add(digit[0])
                                }
                                onCodeChange(newCode.joinToString(""))
                            }
                            newValue.isEmpty() -> {
                                // Удаление
                                if (i < code.length) {
                                    val newCode = code.toMutableList()
                                    newCode.removeAt(i)
                                    onCodeChange(newCode.joinToString(""))
                                }
                            }
                        }
                    },
                    enabled = enabled,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    color = if (isError) Color.Red.copy(alpha = 0.1f) else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Surface(
                                modifier = Modifier.size(48.dp, 54.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = 1.5.dp,
                                    color = when {
                                        isError -> Color.Red
                                        i < code.length -> MaterialTheme.colorScheme.primary
                                        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                                    }
                                ),
                                color = Color.Transparent
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    // ПОКАЗЫВАЕМ ТОЛЬКО ЦИФРУ, БЕЗ innerTextField
                                    if (i < code.length) {
                                        Text(
                                            text = code[i].toString(),
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = if (isError) Color.Red else MaterialTheme.colorScheme.primary
                                        )
                                    } else {
                                        // Показываем innerTextField только для пустого поля
                                        innerTextField()
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

private suspend fun verifyCode(code: String, onResult: (Boolean) -> Unit) {
    delay(1500)
    // TODO: Реальный запрос к Supabase
    onResult(code == "123456")
}