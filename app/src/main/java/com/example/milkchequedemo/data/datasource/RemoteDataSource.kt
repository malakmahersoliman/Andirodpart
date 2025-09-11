package com.example.milkchequedemo.data.datasource


import com.example.milkchequedemo.data.dto.MenuItemDto
import com.example.milkchequedemo.data.dto.SessionRequestDto
import com.example.milkchequedemo.data.dto.SessionResponseDto
import com.example.milkchequedemo.data.dto.StoreInfoResponseDto
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getStoreInfo(
        storeId: Int,
        tableId: Int
    ): Response<StoreInfoResponseDto>

    suspend fun getMenu(storeId: Int,
                        tableId: Int): Response<List<MenuItemDto>>
    suspend fun initSession(session: SessionRequestDto): Response<SessionResponseDto>

}