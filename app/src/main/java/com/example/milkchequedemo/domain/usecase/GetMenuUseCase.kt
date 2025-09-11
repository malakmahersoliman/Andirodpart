package com.example.milkchequedemo.domain.usecase

import com.example.milkchequedemo.domain.model.MenuItem
import com.example.milkchequedemo.domain.repository.MenuRepository
import com.example.milkchequedemo.utils.ResponseWrapper
import javax.inject.Inject

class GetMenuUseCase @Inject constructor(
    private val repo: MenuRepository
) {
    suspend operator fun invoke(storeId: Int,
                                tableId: Int): ResponseWrapper<List<MenuItem>> = repo.getMenu(
        storeId = storeId, tableId = tableId

    )
}
