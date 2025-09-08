package com.example.milkchequedemo.data.repositoryImpl

import com.example.milkchequedemo.data.datasource.RemoteDataSource
import com.example.milkchequedemo.data.mapper.toDomain
import com.example.milkchequedemo.domain.model.StoreInfo
import com.example.milkchequedemo.domain.repository.StoreRepository
import com.example.milkchequedemo.utils.ResponseWrapper
import com.example.milkchequedemo.utils.ResponseWrapper.*
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class StoreRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource
) : StoreRepository {

    override suspend fun getStoreInfo(
        storeId: Int,
        tableId: Int
    ): Flow<ResponseWrapper<StoreInfo>> = flow {
        emit(Loading) // optional; remove if you only want final state
        try {
            val resp = remote.getStoreInfo(storeId, tableId)
            if (resp.isSuccessful) {
                val body = resp.body()
                if (body != null) {
                    emit(Success(body.toDomain()))
                } else {
                    emit(Error(message = "Empty response body", code = resp.code()))
                }
            } else {
                emit(
                    Error(
                        message = resp.errorBody()?.string().orEmpty().ifBlank { resp.message() },
                        code = resp.code()
                    )
                )
            }
        } catch (e: Exception) {
            emit(Error(message = e.message ?: "Unknown error", code = -1))
        }
    }.flowOn(Dispatchers.IO)
}
