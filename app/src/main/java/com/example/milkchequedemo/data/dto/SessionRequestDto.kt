package com.example.milkchequedemo.data.dto

data class SessionRequestDto(
    val firstName: String,
    val customerEmail: String,
    val storeId: String,
    val tableId: String
)
