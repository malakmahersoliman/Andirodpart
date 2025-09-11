package com.example.milkchequedemo.presentation.description

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.milkchequedemo.domain.model.CartModel
import com.example.milkchequedemo.domain.model.MenuItem
import com.example.milkchequedemo.domain.model.MyCart
import com.example.milkchequedemo.presentation.screens.DescriptionScreen
import com.example.milkchequedemo.presentation.viewmodel.DescriptionViewModel

@Composable
fun DescriptionRoute(
    onBack: () -> Unit,
    item: MenuItem,
    showDialog:()-> Unit,
) {
    val vm: DescriptionViewModel = hiltViewModel()
    val state by vm.state.collectAsState()

    DescriptionScreen(
        item=item,
        state = state,
        onBack = onBack,
        onSelectSize = vm::onSelectSize,
        onToggleAddOn = vm::onToggleAddOn,
        onDecQty = vm::onDec,
        onIncQty = vm::onInc,
        onDelete = { /* remove from cart */ },
        onAddToCart = { qnt->
            MyCart.addItem(
                CartModel(
                    id = item.id,
                    name = item.name,
                    price = item.price,
                    qnt =qnt, description = item.description,
                    iconUrl = item.iconUrl,
                )
            )
        },
        showDialog=showDialog
    )
}
