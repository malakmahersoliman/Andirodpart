package com.example.milkchequedemo.domain.usecase



import com.example.milkchequedemo.domain.repository.StoreRepository
import javax.inject.Inject

class GetStoreInfoUseCase @Inject constructor(
    private val repo: StoreRepository
) {
    suspend operator fun invoke(storeId: Int, tableId: Int) =
        repo.getStoreInfo(storeId, tableId)
}