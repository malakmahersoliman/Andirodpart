
package com.example.milkchequedemo.data.mapper


import com.example.milkchequedemo.data.dto.MenuItemDto
import com.example.milkchequedemo.domain.model.MenuItem

fun MenuItemDto.toDomain() = MenuItem(
    id = menuItemId,
    name = menuItemName,
    price = price,
    description = menuItemDescription,
    iconUrl = menuItemIcon
)
