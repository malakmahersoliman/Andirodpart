package com.example.milkchequedemo.data.datasource

import com.example.milkchequedemo.data.dto.AllOrdersResponse
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

    override suspend fun getAllOrders(sessionId: String): Response<List<AllOrdersResponse>>
    = storeApi.getAllOrders(sessionId)
}
