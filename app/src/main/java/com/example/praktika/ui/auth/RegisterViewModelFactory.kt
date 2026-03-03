package com.example.praktika.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory для создания RegisterViewModel
 * @author Студент
 * @date 02.03.2026
 */
class RegisterViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}