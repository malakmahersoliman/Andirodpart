package com.example.milkchequedemo.data.dto

import com.example.milkchequedemo.domain.model.StoreInfo

fun StoreInfoResponseDto.toDomain(): StoreInfo {
    return StoreInfo(
        storeName = this.storeName,
        storeLocation = this.storeLocation,
        tableId = this.tableId
    );
}
//displayed data only as if i revice  10 data from backend
