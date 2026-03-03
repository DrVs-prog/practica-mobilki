package com.example.praktika.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.praktika.ui.auth.ForgotPasswordScreen
import com.example.praktika.ui.auth.RegisterScreen
import com.example.praktika.ui.auth.SignInScreen

/**
 * Класс для управления навигацией в приложении
 * @author Студент
 * @date 02.03.2026
 */
sealed class Screen(val route: String) {
    object Register : Screen("register")
    object SignIn : Screen("sign_in")
    object ForgotPassword : Screen("forgot_password")
    object Verification : Screen("verification")
    object CreateNewPassword : Screen("create_new_password")
}
@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Register.route
    ) {
        composable(Screen.Register.route) {
            RegisterScreen(
                onSignInClick = {
                    navController.navigate(Screen.SignIn.route)
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.SignIn.route) {
            SignInScreen(
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }

        // Пункт 18: Добавляем экран ForgotPassword
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = {
                    navController.popBackStack() // Пункт 21: возврат на Sign In
                },
                onSendSuccess = {
                    navController.navigate(Screen.Verification.route) // Пункт 20: переход на OTP
                }
            )
        }
    }
}