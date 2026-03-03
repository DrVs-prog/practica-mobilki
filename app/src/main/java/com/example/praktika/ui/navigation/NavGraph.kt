package com.example.praktika.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.praktika.ui.auth.RegisterScreen
import com.example.praktika.ui.auth.SignInScreen
import com.example.praktika.ui.auth.ForgotPasswordScreen
import com.example.praktika.ui.auth.VerificationScreen
import com.example.praktika.ui.auth.CreateNewPasswordScreen

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

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSendSuccess = {
                    navController.navigate(Screen.Verification.route)
                }
            )
        }

        // Пункт 23: Экран Verification
        composable(Screen.Verification.route) {
            VerificationScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSuccess = {
                    navController.navigate(Screen.CreateNewPassword.route)
                }
            )
        }

        // Пункт 27: Экран Create New Password
        composable(Screen.CreateNewPassword.route) {
            CreateNewPasswordScreen(
                onSuccess = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.ForgotPassword.route) { inclusive = true }
                    }
                }
            )
        }
    }
}