package com.example.milkchequedemo.data.datasource

import com.example.milkchequedemo.data.dto.AllOrdersResponse
import com.example.milkchequedemo.data.dto.CustomerOrderRequestDto
import com.example.milkchequedemo.data.dto.CustomerOrderResponseDto
import com.example.milkchequedemo.data.dto.MenuItemDto
import com.example.milkchequedemo.data.dto.SessionRequestDto
import com.example.milkchequedemo.data.dto.SessionResponseDto
import com.example.milkchequedemo.data.dto.StoreInfoResponseDto
import com.example.milkchequedemo.data.service.StoreApi
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val storeApi: StoreApi
) : RemoteDataSource {
    override suspend fun getStoreInfo(
        storeId: Int,
        tableId: Int
    ): Response<StoreInfoResponseDto> = storeApi.getStoreInfo(storeId, tableId)

    override suspend fun getMenu(storeId: Int,
                                 tableId: Int): Response<List<MenuItemDto>> = storeApi.getMenu(
        storeId = storeId, tableId = tableId
    )
    override suspend fun initSession(session: SessionRequestDto
    ): Response<SessionResponseDto> = storeApi.initSession(session)

    override suspend fun customerOrder(customerOrderRequestDto: CustomerOrderRequestDto): Response<CustomerOrderResponseDto>
    = storeApi.sessionOrder(customerOrderRequestDto)

    override suspend fun getAllOrders(sessionId: String): Response<List<AllOrdersResponse>>
    = storeApi.getAllOrders(sessionId)

    override suspend fun pay(
        amountCents: Int,
        merchantOrderId: String,
        email: String
    ): Response<String> = storeApi.pay(amountCents, merchantOrderId, email)

}
