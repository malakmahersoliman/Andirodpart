//package com.example.milkchequedemo.presentation.navigation
//
//object Routes {
//    const val LOGIN  = "login"
//    const val SIGNUP = "signup"
//    const val FORGOT = "forgot"
//    const val VERIFY = "verify"          // (optional, if you’ll add it later)
//    const val NEW_PASS = "new_password"  // (optional)
//    const val DIGITAL_MENU = "digital_menu"
//    const val SCAN_ORDER = "scan_order"
//}


package com.example.milkchequedemo.presentation.navigation

object Routes {
    const val Scan = "scanS"

    const val Welcome = "welcome/{storeId}/{tableId}"
    fun welcome(storeId: Int, tableId: Int) = "welcome/$storeId/$tableId"

    const val Menu = "menu/{storeId}/{tableId}"
    fun menu(storeId: Int, tableId: Int) = "menu/$storeId/$tableId"

    // NEW
    const val Description = "description/{item}/{storeId}/{tableId}"
    fun description(item: String,storeId: Int, tableId: Int) = "description/$item/$storeId/$tableId"



    const val Cart = "cart"
    const val Order = "order"
}
