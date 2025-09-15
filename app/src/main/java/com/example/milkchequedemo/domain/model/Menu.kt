package com.example.milkchequedemo.domain.model

data class Menu(
    val menu: List<MenuItem>?=null,
    val categories: List<CategoryResponse>?=null
)