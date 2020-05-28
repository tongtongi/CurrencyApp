package com.example.currencyapp.commons

sealed class Status<D, E> {
    class Success<D, E>(val data: D) : Status<D, E>()
    class Error<D, E>(val error: E) : Status<D, E>()
}