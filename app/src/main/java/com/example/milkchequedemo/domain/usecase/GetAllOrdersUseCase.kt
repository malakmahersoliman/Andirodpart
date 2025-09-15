package com.example.milkchequedemo.domain.usecase

import com.example.milkchequedemo.data.dto.AllOrdersResponse
import com.example.milkchequedemo.domain.model.MenuItem
import com.example.milkchequedemo.domain.repository.MenuRepository
import com.example.milkchequedemo.utils.ResponseWrapper
import javax.inject.Inject

class GetAllOrdersUseCase @Inject constructor(
    private val repo: MenuRepository
) {
    suspend operator fun invoke(id: String): ResponseWrapper<List<AllOrdersResponse>> = repo.getAllOrders(id)
}