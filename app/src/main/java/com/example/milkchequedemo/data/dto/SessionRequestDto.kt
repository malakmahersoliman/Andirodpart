package com.example.milkchequedemo.data.dto

data class SessionRequestDto(
    val customerDOB: String,
    val customerPassword: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val storeId: String,
    val tableId: String
)

