//package com.example.milkchequedemo.presentation.navigation
//
//import androidx.compose.runtime.Composable
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.rememberNavController
//import com.example.milkchequedemo.presentation.screens.*
//import com.example.milkchequedemo.R
//
//
//@Composable
//fun AppNavGraph(
//    navController: NavHostController = rememberNavController(),
//    startDestination: String = Routes.LOGIN
//) {
//    NavHost(
//        navController = navController,
//        startDestination = startDestination
//    ) {
//        // -------- Login --------
//        composable(Routes.LOGIN) {
//            LoginScreen(
//                onLogin = { phone, pass ->
//                    // TODO: validate -> if success go to home
//                    navController.navigate(Routes.DIGITAL_MENU) {
//                        popUpTo(Routes.LOGIN) { inclusive = true } // remove login from back stack
//                        launchSingleTop = true
//                    }
//                },
//                onForgotPassword = {
//                    navController.navigate(Routes.FORGOT)
//
//                },
//                onSignUp = {
//                    navController.navigate(Routes.SIGNUP)
//                }
//
//            )
//        }
//
//        // -------- Signup --------
//        composable(Routes.SIGNUP) {
//            SignupScreen(
//                onSignUp = { name, email, phone, pass ->
//                    // TODO: call signup; then maybe go to verification
//                    navController.navigate(Routes.VERIFY)
//                },
//                onLoginClick = { navController.navigate(Routes.LOGIN)  },
//                onContinueAsGuest = {
//                    navController.navigate(Routes.DIGITAL_MENU) {
//                        popUpTo(Routes.LOGIN) { inclusive = true }
//                    }
//                }
//
//            )
//        }
//
//        // -------- Forgot Password --------
//        composable(Routes.FORGOT) {
//            ForgetPasswordScreen(
//                email = "",
//                onEmailChange = { /* hoist to VM if needed */ },
//                onSendClick = {
//                    // TODO: trigger reset email, then go to NewPassword/Verify screen if you have one
//                    navController.popBackStack() // back to Login after sending
//                },
//                onBackClick = { navController.popBackStack() },
//                onBackToSignUpClick = { navController.navigate(Routes.SIGNUP) }
//            )
//        }
//
//        // -------- Optional screens wired now so navigation compiles --------
//        composable(Routes.VERIFY) { VerificationScreenStub(onDone = {
//            navController.navigate(Routes.DIGITAL_MENU) {
//                popUpTo(Routes.LOGIN) { inclusive = true }
//            }
//        }) }
//
//        composable(Routes.NEW_PASS) { NewPasswordScreenStub(onDone = {
//            navController.navigate(Routes.LOGIN) {
//                popUpTo(Routes.LOGIN) { inclusive = true }
//            }
//        }) }
//
//        // -------- Your existing feature screens --------
////        composable(Routes.DIGITAL_MENU) { DigitalMenu() }
//
////        composable(Routes.SCAN_ORDER) {
////            ScanOrderScreen(
////                qrImageRes = R.drawable.qr_placeholder,   // put a placeholder in res/drawable
////                onScanClick = {
////                    // TODO start camera / scanner flow or navigate
////                    // navController.navigate(Routes.DIGITAL_MENU)
////                }
////            )
////        }
//    }
//}
//
//// --- Simple stubs so the graph compiles if you haven't added these yet ---
//@Composable private fun VerificationScreenStub(onDone: () -> Unit) { /* TODO */ }
//@Composable private fun NewPasswordScreenStub(onDone: () -> Unit) { /* TODO */ }


// presentation/navigation/AppNavGraph.kt
package com.example.milkchequedemo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.milkchequedemo.presentation.menu.MenuRoute
import com.example.milkchequedemo.presentation.description.DescriptionRoute
import com.example.milkchequedemo.presentation.screens.ScanOrderScreen
import com.example.milkchequedemo.presentation.screens.WelcomeScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Routes.Scan) {

        // 1) Scan → emits a route; navigate to Welcome with IDs
        composable(Routes.Scan) {
            ScanOrderScreen { route -> navController.navigate(route) }
            // Your Scan screen should call nav with Routes.welcome(storeId, tableId)
        }

        // 2) Welcome → Continue -> Menu(storeId)
        composable(
            route = Routes.Welcome,
            arguments = listOf(
                navArgument("storeId") { type = NavType.IntType },
                navArgument("tableId") { type = NavType.IntType }
            )
        ) { backStack ->
            val storeId = backStack.arguments?.getInt("storeId") ?: return@composable
            val tableId = backStack.arguments?.getInt("tableId") ?: return@composable

            WelcomeScreen(
                storeId = storeId,
                tableId = tableId,
                onContinue = {
                    navController.navigate(Routes.menu(storeId)) {
                        popUpTo(Routes.Welcome) { inclusive = true } // drop Welcome
                        launchSingleTop = true
                    }
                }
            )
        }

        // 3) Menu -> shows list from API; tap → Description
        composable(
            route = Routes.Menu,
            arguments = listOf(navArgument("storeId") { type = NavType.IntType })
        ) { backStack ->
            val storeId = backStack.arguments!!.getInt("storeId")

            MenuRoute(
                onOpenProduct = { productId ->
                    navController.navigate(Routes.description(storeId, productId))
                },
                onViewCart = { /* navController.navigate("cart") */ }
            )
        }

        // 4) Description (product details)
        composable(
            route = Routes.Description,
            arguments = listOf(
                navArgument("storeId") { type = NavType.IntType },
                navArgument("productId") { type = NavType.StringType }
            )
        ) {
            DescriptionRoute(onBack = { navController.popBackStack() })
        }
    }
}
