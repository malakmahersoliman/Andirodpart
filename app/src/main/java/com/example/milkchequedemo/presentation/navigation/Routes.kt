package com.example.milkchequedemo.presentation.navigation

object Routes {
    const val Scan = "scanS"

    const val Welcome = "welcome/{storeId}/{tableId}"
    fun welcome(storeId: Int, tableId: Int) = "welcome/$storeId/$tableId"

    const val Menu = "menu/{storeId}/{tableId}"
    fun menu(storeId: Int, tableId: Int) = "menu/$storeId/$tableId"

    const val Description = "description/{item}/{storeId}/{tableId}"
    fun description(item: String, storeId: Int, tableId: Int) = "description/$item/$storeId/$tableId"

    const val Cart = "cart"

    // Shows the order tracking list
    const val Order = "order"

    // The “entry to payment” screen
    const val PayEntry = "pay_entry"

    // The actual payment screen
    const val Payment = "payment"
}

