package com.example.milkchequedemo.data.repositoryImpl

import com.example.milkchequedemo.data.datasource.RemoteDataSource
import com.example.milkchequedemo.data.dto.toDomain
import com.example.milkchequedemo.domain.model.StoreInfo
import com.example.milkchequedemo.domain.repository.StoreRepository
import com.example.milkchequedemo.utils.ResponseWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource
) : StoreRepository {

    override suspend fun getStoreInfo(storeId: Int, tableId: Int): Flow<ResponseWrapper<StoreInfo>> = flow {
        emit(ResponseWrapper.Loading)
        try {
            val resp = remote.getStoreInfo(storeId, tableId)
            if (resp.isSuccessful) {
                val body = resp.body()
                if (body != null) {
                    emit(ResponseWrapper.Success(body.toDomain()))
                } else {
                    emit(ResponseWrapper.Error("Empty response body"))
                }
            } else {
                val msg = buildString {
                    append("HTTP ${resp.code()}")
                    resp.message().takeIf { it.isNotBlank() }?.let { append(": ").append(it) }
                }
                emit(ResponseWrapper.Error(msg))
            }
        } catch (e: Exception) {
            emit(ResponseWrapper.Error(e.message ?: "Unknown error"))
        }
    }
}