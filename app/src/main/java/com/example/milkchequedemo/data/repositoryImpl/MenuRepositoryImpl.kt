package com.example.milkchequedemo.data.repositoryImpl

import com.example.milkchequedemo.data.datasource.RemoteDataSource
import com.example.milkchequedemo.data.mapper.toDomain
import com.example.milkchequedemo.domain.model.MenuItem
import com.example.milkchequedemo.domain.repository.MenuRepository
import com.example.milkchequedemo.utils.ResponseWrapper
import com.example.milkchequedemo.utils.ResponseWrapper.*
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource
) : MenuRepository {

    @Volatile private var cache: List<MenuItem>? = null

    override suspend fun getMenu(): ResponseWrapper<List<MenuItem>> {
        return try {
            val resp = remote.getMenu()
            if (resp.isSuccessful) {
                val body = resp.body().orEmpty().map { it.toDomain() }
                cache = body
                Success(body) // repo returns a final result (never Loading/Idle)
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

    override suspend fun findMenuItem(id: Long): ResponseWrapper<MenuItem?> {
        // Fast path: in-memory cache
        cache?.firstOrNull { it.id == id }?.let { return Success(it) }

        // Fallback to network once, then search
        return when (val r = getMenu()) {
            is Success -> {
                val list = r.data ?: emptyList()
                Success(list.firstOrNull { it.id == id })
            }
            is Error -> r
            // These states shouldn’t surface from a suspend repo call,
            // but handle defensively to avoid TODO()/crashes.
            Loading -> Error("Unexpected Loading state in repository", code = -2)
            Idle    -> Error("Unexpected Idle state in repository",    code = -2)
        }
    }
}
