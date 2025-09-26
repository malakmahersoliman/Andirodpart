package com.example.milkchequedemo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.milkchequedemo.domain.model.MenuItem
import com.example.milkchequedemo.domain.model.SessionData
import com.example.milkchequedemo.presentation.description.DescriptionRoute
import com.example.milkchequedemo.presentation.menu.MenuRoute
import com.example.milkchequedemo.presentation.screens.CartScreen
import com.example.milkchequedemo.presentation.screens.EntrypointtoPayScreen
import com.example.milkchequedemo.presentation.screens.OrderTrackingScreen
import com.example.milkchequedemo.presentation.screens.ScanOrderScreen
import com.example.milkchequedemo.presentation.screens.SessionStartDialog
import com.example.milkchequedemo.presentation.screens.WelcomeScreen
import com.example.milkchequedemo.presentation.viewmodel.DescriptionViewModel
import com.google.gson.Gson
import openPaymentUrl

object ItemImage{
    var img:String=""
}
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Routes.Scan) {

        // 1) Scan → emits a route; navigate to Welcome with IDs
        composable(Routes.Scan) {
            ScanOrderScreen { route -> navController.navigate(route) }
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
                    navController.navigate(Routes.menu(storeId,tableId)) {
                        popUpTo(Routes.Welcome) { inclusive = true } // drop Welcome
                        launchSingleTop = true
                    }
                }
            )
        }


        composable(
            route = Routes.Menu,
            arguments = listOf(navArgument("storeId") { type = NavType.IntType },
                navArgument("tableId") { type = NavType.IntType },)
        ) { backStack ->
            val storeId = backStack.arguments!!.getInt("storeId")
            val tableId = backStack.arguments!!.getInt("tableId")

            MenuRoute(
                onOpenProduct = { item ->
                    ItemImage.img= item.iconUrl.toString()
                    navController.navigate(Routes.description(item=Gson().toJson(
                        item.copy(iconUrl = ""),
                    ), storeId = storeId, tableId = tableId
                    ))
                },
                onViewCart = {
                    navController.navigate(
                        Routes.cart(
                            storeId = storeId,
                            customerId = SessionData.customerId ?: return@MenuRoute
                        )
                    )
                },
                storeId = storeId,
                tableId = tableId,
                )
        }


        composable(
            route = Routes.Description,
            arguments = listOf(
                navArgument("item") { type = NavType.StringType },
                navArgument("storeId") { type = NavType.IntType },
                navArgument("tableId") { type = NavType.IntType },
            )
        ) { backStack ->
            val storeId =   backStack.arguments!!.getInt("storeId")
            val tableId =  backStack.arguments!!.getInt("tableId")
            val viewModel= hiltViewModel<DescriptionViewModel>()
            val session=viewModel.session.collectAsState()
            val showDialog= remember {
                mutableStateOf(false)
            }
            val item: MenuItem =
                Gson().fromJson( backStack.arguments!!.getString("item"), MenuItem::class.java)

            if(session.value!=null){
                showDialog.value=false
            }

            DescriptionRoute(item=item,onBack = { navController.navigate(
                Routes.menu(storeId,tableId)
            ) }, showDialog = {
                if(SessionData.token==null) {
                    showDialog.value = true
                }
            })

            if(showDialog.value){
                SessionStartDialog(onDismiss = {

                }, onConfirm = {name,mail->

                    viewModel.load(
                        userName = name?:"", mail = mail?:"", storeId = storeId.toString(), tableId = tableId.toString()
                    )
                }
                )
            }
        }

        composable(
            route = Routes.Order,
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { entry ->
            val orderId = entry.arguments?.getInt("orderId")
            SessionData.orderId = orderId
            OrderTrackingScreen(
                onBack = { navController.popBackStack() },
                navigateToWebPage = { url ->
                    openPaymentUrl(navController.context, url)
                }
            )
        }

        composable(
            route = Routes.Cart,
            arguments = listOf(
                navArgument("storeId") { type = NavType.IntType },
                navArgument("customerId") { type = NavType.IntType }
            )
        ) { entry ->
            val storeId = entry.arguments?.getInt("storeId") ?: return@composable
            val customerId = entry.arguments?.getInt("customerId") ?: return@composable

            CartScreen(
                onOrderSuccess = { orderId: String ->
                    SessionData.orderId = orderId.toIntOrNull()
                    navController.navigate(Routes.order(orderId.toInt()))
                },
                onBack = { navController.popBackStack() },
                customerId = customerId.toString(),
                storeId = storeId.toString(),
            )
        }

        composable(route = Routes.PayEntry) {
            EntrypointtoPayScreen(
                onEditClick = {

                    val popped = navController.popBackStack(Routes.Cart, inclusive = false)
                    if (!popped) {
                        navController.navigate(Routes.Cart) {
                            launchSingleTop = true
                        }
                    }
                },
                onProceedClick = {
                    navController.navigate(Routes.Payment) {
                        launchSingleTop = true
                    }
                }
            )
        }

//removed
//        composable(route = Routes.Payment) {
//            // TODO: replace with real data (e.g., from shared VM) once ready
//            val fake = PaymentUiState(
//                tableLabel = "Table #5",
//                mode = PayMode.Individually,
//                payers = listOf(
//                    PayerVM("p1", "Malak", "590.99 L.E."),
//                    PayerVM("p2", "Rola", "368.99 L.E.")
//                ),
//                grandTotalText = "959.98 L.E."
//            )
//
//            PaymentScreen(
//                state = fake,
//                onBack = { navController.popBackStack() },   // back to Entry
//                onModeChange = { /* update VM later */ },
//                onPayPerson = { _, _ -> /* handle later */ },
//                onPayAll = { /* handle later */ }
//            )
//        }

    }
}
