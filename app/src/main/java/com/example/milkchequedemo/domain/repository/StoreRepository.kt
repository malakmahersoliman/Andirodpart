package com.example.milkchequedemo.domain.repository

import com.example.milkchequedemo.domain.model.StoreInfo
import com.example.milkchequedemo.utils.ResponseWrapper
import kotlinx.coroutines.flow.Flow

interface StoreRepository {
    suspend fun getStoreInfo(storeId: Int, tableId: Int): Flow<ResponseWrapper<StoreInfo>>
}


