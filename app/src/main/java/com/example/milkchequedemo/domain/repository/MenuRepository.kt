package com.example.milkchequedemo.domain.repository


import com.example.milkchequedemo.data.dto.AllOrdersResponse
import com.example.milkchequedemo.data.dto.CustomerOrderResponseDto
import com.example.milkchequedemo.domain.model.CategoryResponse
import com.example.milkchequedemo.domain.model.MenuItem
import com.example.milkchequedemo.utils.ResponseWrapper
import retrofit2.Response

interface MenuRepository {
    suspend fun getMenu(storeId: Int,
                        tableId: Int): ResponseWrapper<List<MenuItem>>
    suspend fun findMenuItem(id: Long): ResponseWrapper<MenuItem?>

    suspend fun getAllOrders(sessionId: String): ResponseWrapper<List<AllOrdersResponse>>
    suspend fun customerOrder( customerId: String,
                               orderItems:Map<String, String> ,//id, qnt
                               sessionId: String,
                               storeId: String,
                               token: String): ResponseWrapper<Int>//todo


    suspend fun pay(
        amountCents: Int,
        merchantOrderId: String,
        otherMerchantsOrderId: List<Int> = emptyList()
    ): ResponseWrapper<String>
}