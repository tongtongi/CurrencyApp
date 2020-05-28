package com.example.currencyapp.commons


sealed class Result<out T> {
    class Success<T>(val value: T) : Result<T>()
    class Failure(val model: ErrorModel) : Result<Nothing>()
}

class ErrorModel(val code: Int, val message: String = "") {
    companion object {
        val DEFAULT = ErrorModel(-1)
    }
}

fun <T> Result<T>.handleResult(onSuccess: (T) -> Unit, onFailure: (ErrorModel) -> Unit) {
    when (this) {
        is Result.Success -> onSuccess(this.value)
        is Result.Failure -> onFailure(this.model)
    }
}