// domain/repository/MenuRepository.kt
package com.example.milkchequedemo.domain.repository


import com.example.milkchequedemo.domain.model.MenuItem
import com.example.milkchequedemo.utils.ResponseWrapper

interface MenuRepository {
    suspend fun getMenu(storeId: Int,
                        tableId: Int): ResponseWrapper<List<MenuItem>>
    suspend fun findMenuItem(id: Long): ResponseWrapper<MenuItem?>
}