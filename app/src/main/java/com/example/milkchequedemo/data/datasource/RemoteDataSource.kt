package com.example.milkchequedemo.data.datasource


import com.example.milkchequedemo.data.dto.AllOrdersResponse
import com.example.milkchequedemo.data.dto.CustomerOrderRequestDto
import com.example.milkchequedemo.data.dto.CustomerOrderResponseDto
import com.example.milkchequedemo.data.dto.MenuItemDto
import com.example.milkchequedemo.data.dto.SessionRequestDto
import com.example.milkchequedemo.data.dto.SessionResponseDto
import com.example.milkchequedemo.data.dto.StoreInfoResponseDto
import com.example.milkchequedemo.domain.model.Session
import retrofit2.Response
import retrofit2.http.Query

interface RemoteDataSource {
    suspend fun getStoreInfo(
        storeId: Int,
        tableId: Int
    ): Response<StoreInfoResponseDto>

    suspend fun getMenu(storeId: Int,
                        tableId: Int): Response<List<MenuItemDto>>
    suspend fun initSession(session: SessionRequestDto): Response<SessionResponseDto>

    suspend fun customerOrder(customerOrderRequestDto: CustomerOrderRequestDto): Response<CustomerOrderResponseDto>

    suspend fun getAllOrders(sessionId:String): Response<List<AllOrdersResponse>>
    suspend fun pay(amountCents: Int,merchantOrderId: String, email: String): Response<String>


}