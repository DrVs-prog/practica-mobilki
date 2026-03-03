package com.example.praktika.ui.auth

import android.app.AlertDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    onSendSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заголовок
        Text(
            text = "Восстановление пароля",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Подзаголовок
        Text(
            text = "Введите ваш email для получения кода",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Поле Email
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Email",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("xyz@gmail.com") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                enabled = !isLoading
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка отправки
        Button(
            onClick = {
                if (validateEmail(email, context)) {
                    isLoading = true
                    coroutineScope.launch {
                        // Имитация запроса к серверу
                        delay(2000)
                        isLoading = false

                        // Показать диалог успеха
                        showSuccessDialog(context) {
                            onSendSuccess() // Переход на OTP Verification
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !isLoading,
            shape = MaterialTheme.shapes.medium
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = "Отправить",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

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

private fun validateEmail(email: String, context: android.content.Context): Boolean {
    val emailPattern = "^[a-z0-9]+@[a-z0-9]+\\.[a-z]{3,}$"

    return if (email.isEmpty()) {
        showErrorDialog(context, "Email не может быть пустым")
        false
    } else if (!Regex(emailPattern).matches(email)) {
        showErrorDialog(context, "Неверный формат email")
        false
    } else {
        true
    }
}

private fun showErrorDialog(context: android.content.Context, message: String) {
    AlertDialog.Builder(context)
        .setTitle("Ошибка")
        .setMessage(message)
        .setPositiveButton("OK", null)
        .show()
}

// Диалог успеха
private fun showSuccessDialog(context: android.content.Context, onOkClick: () -> Unit) {
    AlertDialog.Builder(context)
        .setTitle("Успешно")
        .setMessage("Код для восстановления пароля отправлен на ваш email")
        .setPositiveButton("OK") { _, _ ->
            onOkClick()
        }
        .setCancelable(false)
        .show()
}