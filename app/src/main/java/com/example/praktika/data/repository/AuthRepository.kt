package com.example.praktika.data.repository


import android.content.Context
import com.example.praktika.common.SupabaseConfig
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Репозиторий для работы с аутентификацией через Supabase
 * @author Студент
 * @date 02.03.2026
 */
class AuthRepository(private val context: Context) {

    /**
     * Регистрация нового пользователя
     */
    suspend fun registerUser(
        email: String,
        password: String,
        name: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Регистрация в Supabase Auth
            SupabaseConfig.client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            // TODO: Сохранить имя пользователя в отдельную таблицу profiles

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Вход пользователя
     */
    suspend fun signInUser(
        email: String,
        password: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            SupabaseConfig.client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Выход пользователя
     */
    suspend fun signOut(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            SupabaseConfig.client.auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}