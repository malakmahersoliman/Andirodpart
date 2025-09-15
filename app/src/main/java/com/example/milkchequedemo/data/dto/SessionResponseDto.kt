package com.example.milkchequedemo.data.dto

data class SessionResponseDto(
    val code: String,
    val message: String,
    val token: String?,
    val customerId:Int,
    val sessionId:Int,
)
