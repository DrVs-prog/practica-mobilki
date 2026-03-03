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
fun RegisterScreen(
    onSignInClick: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var agreedToTerms by remember { mutableStateOf(false) }
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
            text = "Регистрация",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Подзаголовок
        Text(
            text = "Заполните Свои Данные",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Поле Ваше имя
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Ваше имя",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("xxxxxxxxx") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                singleLine = true,
                enabled = !isLoading
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Поле Email
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Email",
                fontSize = 14.sp,
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

        Spacer(modifier = Modifier.height(24.dp))

        // Поле Пароль
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Пароль",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
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
                enabled = !isLoading
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Чекбокс
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = agreedToTerms,
                onCheckedChange = { agreedToTerms = it },
                enabled = !isLoading
            )
            Text(
                text = "Даю согласие на обработку персональных данных",
                fontSize = 14.sp,
                modifier = Modifier.clickable { agreedToTerms = !agreedToTerms }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка регистрации
        Button(
            onClick = {
                if (validateFields(name, email, password, context)) {
                    isLoading = true
                    coroutineScope.launch {
                        // Имитация запроса к серверу
                        delay(2000)

                        // Всегда успешно для демонстрации
                        isLoading = false
                        Toast.makeText(context, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                        onRegisterSuccess()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = agreedToTerms && !isLoading,
            shape = MaterialTheme.shapes.medium
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = "Зарегистрироваться",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Ссылка на вход
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSignInClick() },
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Есть аккаунт? ",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = "Войти",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun validateFields(name: String, email: String, password: String, context: android.content.Context): Boolean {
    val emailPattern = "^[a-z0-9]+@[a-z0-9]+\\.[a-z]{3,}$"

    return if (name.isEmpty()) {
        showErrorDialog(context, "Имя не может быть пустым")
        false
    } else if (email.isEmpty()) {
        showErrorDialog(context, "Email не может быть пустым")
        false
    } else if (!Regex(emailPattern).matches(email)) {
        showErrorDialog(context, "Неверный формат email. Используйте: имя@домен.домен (только маленькие буквы и цифры)")
        false
    } else if (password.isEmpty()) {
        showErrorDialog(context, "Пароль не может быть пустым")
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
        .setCancelable(false)
        .show()
}