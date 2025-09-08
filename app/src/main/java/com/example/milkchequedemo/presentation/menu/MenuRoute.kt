package com.example.milkchequedemo.presentation.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import com.example.milkchequedemo.presentation.screens.DigitalMenuScreen
import com.example.milkchequedemo.presentation.screens.BottomTab
import com.example.milkchequedemo.presentation.viewmodel.MenuViewModel

@Composable
fun MenuRoute(
    onOpenProduct: (String) -> Unit,
    onViewCart: () -> Unit
) {
    val vm: MenuViewModel = hiltViewModel()
    val state by vm.state.collectAsState()

    DigitalMenuScreen(
        state = state,
        onBellClick = {},
        onSearchClick = {},
        onSeeAllClick = {},
        onAddItem = { /* start session + add-to-cart if needed */ },
        onProductClick = onOpenProduct,
        onCategoryClick = { /* filter in VM later */ },
        onTabSelected = { /* BottomTab -> handle later */ },
        onViewCartClick = onViewCart
    )
}
