package com.example.milkchequedemo.data.dto

data class CustomerOrderRequestDto(
    val customerId: String,
    val orderItems:Map<String, String> ,//id, qnt
    val sessionId: String,
    val storeId: String,
    val token: String
)

/*
{
    "token": "0",
    "customerId": "240",
    "sessionId": "1",
    "storeId": "1",
    "orderItems": {
        "1": "3",
        "2":"1",
        "4":"4"
    }
}
*/
