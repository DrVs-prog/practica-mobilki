package com.example.praktika.ui.auth


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.praktika.data.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel для экрана регистрации
 * @author Студент
 * @date 02.03.2026
 */
class RegisterViewModel(
    private val context: Context
) : ViewModel() {

    private val repository = AuthRepository(context)

    // Состояния экрана
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isRegistered = MutableStateFlow(false)
    val isRegistered: StateFlow<Boolean> = _isRegistered

    /**
     * Регистрация пользователя
     */
    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val result = repository.registerUser(email, password, name)

                result.onSuccess {
                    _isRegistered.value = true
                }.onFailure { exception ->
                    _errorMessage.value = handleAuthError(exception as Exception)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка сети: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Очистка сообщения об ошибке
     */
    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * Обработка ошибок аутентификации
     */
    private fun handleAuthError(exception: Exception): String {
        return when {
            exception.message?.contains("Email already registered") == true ->
                "Этот email уже зарегистрирован"
            exception.message?.contains("Password should be at least 6 characters") == true ->
                "Пароль должен содержать минимум 6 символов"
            exception.message?.contains("Invalid email") == true ->
                "Неверный формат email"
            else -> "Ошибка регистрации: ${exception.message}"
        }
    }
}