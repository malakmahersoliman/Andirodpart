
package com.example.milkchequedemo.data.service

import com.example.milkchequedemo.data.dto.MenuItemDto
import com.example.milkchequedemo.data.dto.StoreInfoResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StoreApi {
    @GET("store/info")
    suspend fun getStoreInfo(
        @Query("storeId") storeId: Int,
        @Query("tableId") tableId: Int
    ): Response<StoreInfoResponseDto>

    // GET https://.../store/menu
    @GET("store/menu")
    suspend fun getMenu(): Response<List<MenuItemDto>>
}

