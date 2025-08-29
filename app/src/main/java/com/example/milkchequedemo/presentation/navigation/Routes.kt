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
}
