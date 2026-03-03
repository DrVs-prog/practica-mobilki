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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.praktika.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewPasswordScreen(
    onSuccess: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var passwordsMatch by remember { mutableStateOf(true) }

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
            text = "Новый пароль",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Подзаголовок
        Text(
            text = "Придумайте новый пароль",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Поле Новый пароль
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Новый пароль",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    checkPasswordsMatch(password, confirmPassword) { match ->
                        passwordsMatch = match
                    }
                },
                placeholder = { Text("●●●●●●●") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = if (passwordVisible)
                                painterResource(id = R.drawable.ic_visibility_on)
                            else
                                painterResource(id = R.drawable.ic_visibility_off),
                            contentDescription = "visibility"
                        )
                    }
                },
                singleLine = true,
                enabled = !isLoading,
                isError = !passwordsMatch && confirmPassword.isNotEmpty()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Поле Подтверждение пароля
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Подтвердите пароль",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    checkPasswordsMatch(password, confirmPassword) { match ->
                        passwordsMatch = match
                    }
                },
                placeholder = { Text("●●●●●●●") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            painter = if (confirmPasswordVisible)
                                painterResource(id = R.drawable.ic_visibility_on)
                            else
                                painterResource(id = R.drawable.ic_visibility_off),
                            contentDescription = "visibility"
                        )
                    }
                },
                singleLine = true,
                enabled = !isLoading,
                isError = !passwordsMatch && confirmPassword.isNotEmpty()
            )
        }

        // Подсказка о несовпадении паролей (пункт 28)
        if (!passwordsMatch && confirmPassword.isNotEmpty()) {
            Text(
                text = "Пароли не совпадают",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка сохранения (пункт 29, 30, 31, 32)
        Button(
            onClick = {
                if (password.length < 6) {
                    showErrorDialog(context, "Пароль должен содержать минимум 6 символов")
                } else if (!passwordsMatch) {
                    showErrorDialog(context, "Пароли не совпадают")
                } else {
                    isLoading = true
                    coroutineScope.launch {
                        // Пункт 30: Отправка запроса на сервер для изменения пароля
                        changePassword(password) { isSuccess, errorMessage ->
                            isLoading = false
                            if (isSuccess) {
                                Toast.makeText(context, "Пароль успешно изменен", Toast.LENGTH_SHORT).show()
                                onSuccess() // Пункт 29: переход на Sign In
                            } else {
                                // Пункт 31: Отображение ошибки
                                showErrorDialog(context, errorMessage ?: "Ошибка при смене пароля")
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = password.isNotEmpty() && confirmPassword.isNotEmpty() && !isLoading,
            shape = MaterialTheme.shapes.medium
        ) {
            if (isLoading) {
                // Пункт 32: Индикация загрузки
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = "Сохранить",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// Пункт 28: Проверка совпадения паролей
private fun checkPasswordsMatch(password: String, confirmPassword: String, onResult: (Boolean) -> Unit) {
    onResult(password == confirmPassword)
}

// Пункт 30: Имитация запроса на смену пароля
private suspend fun changePassword(newPassword: String, onResult: (Boolean, String?) -> Unit) {
    delay(2000) // Имитация загрузки
    // TODO: Реальный запрос к Supabase
    // Для примера всегда успешно
    onResult(true, null)
}

private fun showErrorDialog(context: android.content.Context, message: String) {
    AlertDialog.Builder(context)
        .setTitle("Ошибка")
        .setMessage(message)
        .setPositiveButton("OK", null)
        .setCancelable(false)
        .show()
}