package com.example.milkchequedemo.domain.usecase

import com.example.milkchequedemo.domain.repository.MenuRepository
import com.example.milkchequedemo.utils.ResponseWrapper
import javax.inject.Inject

class CustomerOrderUseCase @Inject constructor(
    private val repo: MenuRepository
) {
    suspend operator fun invoke( customerId: String,
                                 orderItems:Map<String, String> ,//id, qnt
                                 sessionId: String,
                                 storeId: String,
                                 token: String): ResponseWrapper<Int>//order id
    = repo.customerOrder(
        customerId = customerId,
        orderItems = orderItems,
        sessionId = sessionId,
        storeId = storeId,
        token = token
    )
}