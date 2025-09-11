package com.example.milkchequedemo.data.repositoryImpl

import com.example.milkchequedemo.data.datasource.RemoteDataSource
import com.example.milkchequedemo.data.mapper.toDomain
import com.example.milkchequedemo.data.mapper.toDto
import com.example.milkchequedemo.domain.model.Session
import com.example.milkchequedemo.domain.model.SessionResponse
import com.example.milkchequedemo.domain.repository.SessionRepository
import com.example.milkchequedemo.utils.ResponseWrapper
import com.example.milkchequedemo.utils.ResponseWrapper.Error
import com.example.milkchequedemo.utils.ResponseWrapper.Success
import javax.inject.Inject

class SessionRepositoryImpl
    @Inject constructor(
        private val remote: RemoteDataSource
    ): SessionRepository {
    override suspend fun initSession(session: Session): ResponseWrapper<SessionResponse> {


        return try {
            val resp = remote.initSession(session.toDto())
            if (resp.isSuccessful) {
                val body = resp.body()!!.toDomain()
                    Success(body)
            } else {
                Error(
                    message = resp.errorBody()?.string().orEmpty().ifBlank { resp.message() },
                    code = resp.code()
                )
            }
        } catch (e: Exception) {
            // No HTTP code on exceptions → use a sentinel (e.g., -1)
            Error(
                message = e.message ?: "Unknown error",
                code = -1
            )
        }
    }
}