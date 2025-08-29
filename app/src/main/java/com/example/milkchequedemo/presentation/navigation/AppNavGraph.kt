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


package com.example.milkchequedemo.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.milkchequedemo.presentation.screens.ScanOrderScreen
import com.example.milkchequedemo.presentation.screens.WelcomeScreen
import com.example.milkchequedemo.presentation.viewmodel.WelcomeViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Routes.Scan) {
        composable(Routes.Scan) {
            ScanOrderScreen { route -> navController.navigate(route) }
        }
        composable(
            route = Routes.Welcome,
            arguments = listOf(
                navArgument("storeId") { type = NavType.IntType },
                navArgument("tableId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val storeId = backStackEntry.arguments!!.getInt("storeId")
            val tableId = backStackEntry.arguments!!.getInt("tableId")
            WelcomeScreen(storeId = storeId, tableId = tableId)
        }
    }
}
