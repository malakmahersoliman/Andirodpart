package com.example.milkchequedemo.data.dto

import kotlinx.serialization.Serializable


data class MenuItemDto(
    val menuItemId: Long,
    val menuItemName: String,
    val price: Double,
    val menuItemDescription: String?,
    val menuItemPictureURL: String?,
    val menuItemCategory: Int
)
