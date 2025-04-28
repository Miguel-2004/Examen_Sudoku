package com.app.sudokuapp.presentation.util

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val result: T) : UiState<T>()
    data class Error(val errorMessage: String) : UiState<Nothing>()
}