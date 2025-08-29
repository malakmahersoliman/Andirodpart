package com.example.milkchequedemo.utils

sealed class ResponseWrapper<out T>{
    object Loading: ResponseWrapper<Nothing>()
    object Idle: ResponseWrapper<Nothing>()
    data class Success<out T>(
        val data:T?
    ): ResponseWrapper<T>()
    data class Error(
        val message:String
    ): ResponseWrapper<Nothing>()
}