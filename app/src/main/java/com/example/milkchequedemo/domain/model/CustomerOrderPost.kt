package com.example.milkchequedemo.domain.model

data class CustomerOrderPost(
    val customerId: String,
//    val orderItems: ,
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
