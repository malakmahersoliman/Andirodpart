package com.example.milkchequedemo.data.dto

data class AllOrdersResponse(
    val customerId: Int,
    val customerName: String,
    val orderItems: List<OrderItem>
){
    data class OrderItem(
        val name: String,
        val price: Double,
        val quantity: Int,
        val id: Int=-1
    )
}