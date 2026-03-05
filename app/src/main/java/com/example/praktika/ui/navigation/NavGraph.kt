package com.example.praktika.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.praktika.ui.splash.SplashScreen
import com.example.praktika.ui.onboard.OnboardScreen
import com.example.praktika.ui.auth.RegisterScreen
import com.example.praktika.ui.auth.SignInScreen
import com.example.praktika.ui.auth.ForgotPasswordScreen
import com.example.praktika.ui.auth.VerificationScreen
import com.example.praktika.ui.auth.CreateNewPasswordScreen
import com.example.praktika.ui.home.HomeScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboard : Screen("onboard")
    object Register : Screen("register")
    object SignIn : Screen("sign_in")
    object ForgotPassword : Screen("forgot_password")
    object Verification : Screen("verification")
    object CreateNewPassword : Screen("create_new_password")
    object Home : Screen("home")
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val isFirstLaunch = remember { mutableStateOf(true) }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route // 👈 ВСЕГДА СПЛЭШ
    ) {
        // Splash экран
        composable(Screen.Splash.route) {
            SplashScreen(
                onTimeout = {
                    val destination = if (isFirstLaunch.value) Screen.Onboard.route else Screen.Home.route
                    navController.navigate(destination) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Onboard экран
        composable(Screen.Onboard.route) {
            OnboardScreen(
                onGetStarted = {
                    isFirstLaunch.value = false
                    navController.navigate(Screen.Register.route) {
                        popUpTo(Screen.Onboard.route) { inclusive = true }
                    }
                }
            )
        }

        // Register экран
        composable(Screen.Register.route) {
            RegisterScreen(
                onSignInClick = {
                    navController.navigate(Screen.SignIn.route)
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        // Sign In экран
        composable(Screen.SignIn.route) {
            SignInScreen(
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ForgotPassword.route)
                },
                onSignInSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                }
            )
        }

        // Forgot Password экран
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

        // Verification экран
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

        // Create New Password экран
        composable(Screen.CreateNewPassword.route) {
            CreateNewPasswordScreen(
                onSuccess = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.ForgotPassword.route) { inclusive = true }
                    }
                }
            )
        }

        // Home экран
        composable(Screen.Home.route) {
            HomeScreen()
        }
    }
}