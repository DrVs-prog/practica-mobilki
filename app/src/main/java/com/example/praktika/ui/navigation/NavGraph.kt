package com.example.praktika.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.praktika.ui.splash.SplashScreen
import com.example.praktika.ui.onboard.OnboardScreen
import com.example.praktika.ui.auth.RegisterScreen
import com.example.praktika.ui.auth.SignInScreen
import com.example.praktika.ui.auth.ForgotPasswordScreen
import com.example.praktika.ui.auth.VerificationScreen
import com.example.praktika.ui.auth.CreateNewPasswordScreen
import com.example.praktika.ui.home.HomeScreen
import com.example.praktika.ui.catalog.CatalogScreen
import com.example.praktika.ui.favorite.FavoriteScreen
import com.example.praktika.ui.cart.CartScreen
import com.example.praktika.ui.profile.ProfileScreen
import com.example.praktika.ui.details.DetailsScreen
import com.example.praktika.ui.profile.UserProfile

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboard : Screen("onboard")
    object Register : Screen("register")
    object SignIn : Screen("sign_in")
    object ForgotPassword : Screen("forgot_password")
    object Verification : Screen("verification")
    object CreateNewPassword : Screen("create_new_password")
    object Home : Screen("home")
    object Catalog : Screen("catalog/{category}") {
        fun passCategory(category: String) = "catalog/$category"
    }
    object Favorite : Screen("favorite")
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    object Details : Screen("details/{productId}") {
        fun passProductId(productId: Int) = "details/$productId"
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val isFirstLaunch = remember { mutableStateOf(true) }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // Auth flow
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

        composable(Screen.CreateNewPassword.route) {
            CreateNewPasswordScreen(
                onSuccess = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.ForgotPassword.route) { inclusive = true }
                    }
                }
            )
        }

        // Главный экран с нижней навигацией
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        // Каталог с параметром категории
        composable(
            route = Screen.Catalog.route,
            arguments = listOf(navArgument("category") { defaultValue = "Все" })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "Все"
            CatalogScreen(
                category = category,
                onProductClick = { productId ->
                    navController.navigate(Screen.Details.passProductId(productId))
                }
            )
        }

        // Избранное
        composable(Screen.Favorite.route) {
            FavoriteScreen()
        }

        // Корзина
        composable(Screen.Cart.route) {
            CartScreen()
        }

        // Профиль
        composable(Screen.Profile.route) {
            ProfileScreen(
                userProfile = UserProfile(),
                photoBitmap = null,
                onProfileUpdate = { _, _ -> }
            )
        }

        // Детали товара
        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            DetailsScreen(
                productId = productId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}