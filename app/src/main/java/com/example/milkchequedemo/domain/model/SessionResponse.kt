package com.example.milkchequedemo.domain.model

data class SessionResponse(
    val code: String,
    val message: String,
    val token: String?,
    val customerId:Int,
    val sessionId:Int,
)