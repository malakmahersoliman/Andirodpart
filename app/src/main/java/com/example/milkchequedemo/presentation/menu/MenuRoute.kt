package com.example.milkchequedemo.presentation.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.milkchequedemo.domain.model.MenuItem
import com.example.milkchequedemo.presentation.components.ReusableButton
import com.example.milkchequedemo.presentation.screens.DigitalMenuScreen
import com.example.milkchequedemo.presentation.screens.BottomTab
import com.example.milkchequedemo.presentation.viewmodel.MenuViewModel

@Composable
fun MenuRoute(
    onOpenProduct: (MenuItem) -> Unit,
    onViewCart: () -> Unit,
    storeId: Int,
    tableId: Int
) {
    val vm: MenuViewModel = hiltViewModel()
    val state by vm.state.collectAsState()
    var menu by remember {
        mutableStateOf(state.bestSellers)
    }

    LaunchedEffect(Unit) {
        vm.load(
            storeId = storeId, tableId = tableId
        )
    }
    if(state.isLoading){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    }
    else if(state.error!=null){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(state.error!!)
            ReusableButton(
                onClick = {
                    vm.load(storeId = storeId, tableId = tableId)
                },
                text = "Please Try Again"
            )
        }
    }else {
        DigitalMenuScreen(
            state = state,
            onBellClick = {},
            onSearchClick = {},
            onSeeAllClick = {},
            onAddItem = { /* start session + add-to-cart if needed */ },
            onProductClick = onOpenProduct,
            onCategoryClick ={id->
                if(id==0){
                    menu=state.bestSellers
                }else{
                    menu=state.bestSellers.filter { it.menuItemCategory==id }
                }
            },
            onTabSelected = { /* BottomTab -> handle later */ },
            onViewCartClick = onViewCart,
            items = menu.ifEmpty { state.bestSellers }
        )
    }
}
