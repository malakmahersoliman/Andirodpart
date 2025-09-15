package com.example.milkchequedemo.domain.model

data class CategoryResponse (
    val menuItemCategory: Int,
    val menuItemDescription: String,
    val menuItemId: Int,
    val menuItemName: String,
    val menuItemPictureURL: String,
    val price: Double
)