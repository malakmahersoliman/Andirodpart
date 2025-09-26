package com.example.milkchequedemo.data.dto

data class PaymentRequestDto(
    val amountCents: String,               // e.g., "1000"
    val merchantOrderId: String,           // e.g., "76"
    val otherMerchantsOrderId: List<Int>? = null // null => omit; emptyList() => send []
)
