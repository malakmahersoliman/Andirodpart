package com.example.milkchequedemo.data.datasource


import com.example.milkchequedemo.data.dto.MenuItemDto
import com.example.milkchequedemo.data.dto.StoreInfoResponseDto
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getStoreInfo(
        storeId: Int,
        tableId: Int
    ): Response<StoreInfoResponseDto>

    suspend fun getMenu(): Response<List<MenuItemDto>>
}