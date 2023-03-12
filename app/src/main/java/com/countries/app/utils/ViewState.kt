package com.countries.app.utils

sealed interface ViewState<out T> {
    data class Success<T>(val data:T): ViewState<T>
    data class Failure(val message: String): ViewState<Nothing>
    object Loading: ViewState<Nothing>
}