package com.example.milkchequedemo.data.dto


data class MenuItemDto(
    val menuItemId: Long,
    val menuItemName: String,
    val price: Double,
    val menuItemDescription: String?,
    val menuItemIcon: String?
)