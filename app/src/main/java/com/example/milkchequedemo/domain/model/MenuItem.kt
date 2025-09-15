
package com.example.milkchequedemo.domain.model

data class MenuItem(
    val id: Long,
    val name: String,
    val price: Double,
    val description: String?,
    val iconUrl: String?,
    val menuItemCategory: Int
)
