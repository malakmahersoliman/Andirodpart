package com.example.milkchequedemo.data.datasource

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
}