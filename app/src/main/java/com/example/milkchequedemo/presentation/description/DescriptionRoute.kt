package com.example.milkchequedemo.presentation.description

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.milkchequedemo.presentation.screens.DescriptionScreen
import com.example.milkchequedemo.presentation.viewmodel.DescriptionViewModel

@Composable
fun DescriptionRoute(
    onBack: () -> Unit
) {
    val vm: DescriptionViewModel = hiltViewModel()
    val state by vm.state.collectAsState()

    DescriptionScreen(
        state = state,
        onBack = onBack,
        onSelectSize = vm::onSelectSize,
        onToggleAddOn = vm::onToggleAddOn,
        onDecQty = vm::onDec,
        onIncQty = vm::onInc,
        onDelete = { /* remove from cart */ },
        onAddToCart = { /* add to cart + maybe start session */ }
    )
}
