package com.example.milkchequedemo.domain.usecase

import com.example.milkchequedemo.domain.repository.MenuRepository
import javax.inject.Inject

class PayUseCase @Inject constructor(
    private val repo: MenuRepository
) {
    suspend operator fun invoke(
        amountCents: Int,
        merchantOrderId: String,
        email: String
    ) =
        repo.pay(amountCents = amountCents, merchantOrderId = merchantOrderId, email = email)
}