package com.example.milkchequedemo.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.milkchequedemo.domain.model.MyCart
import com.example.milkchequedemo.presentation.components.ReusableButton

@Composable
fun CartScreen(
    order:()-> Unit
) {
    var cartItems by remember {
        mutableStateOf(MyCart.cart.toList())
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (cartItems.isNotEmpty()) {
            CustomerCard(
                customer = CustomerOrderVM(
                    id = "regione",
                    name = MyCart.name,
                    isSelected = true,
                    lines = cartItems.map { item ->
                        OrderLineVM(
                            id = "id",
                            title = item.name,
                            priceText = item.price.toString(),
                            qty = item.qnt
                        )
                    },
                    totalText = cartItems.sumOf {
                        it.price * it.qnt // Calculate total price correctly
                    }.toString()
                ),
                onToggle = {},
                onSeeAll = {},
                onInc = {line->
                    val itemToInc = MyCart.cart.find { cartModel ->
                        cartModel.name == line.title && cartModel.qnt==line.qty
                    }
                    if (itemToInc != null) {
                        MyCart.update(itemToInc.copy(qnt = itemToInc.qnt+1))
                    }
                    cartItems = MyCart.cart.toList()
                },
                onDec = {
                    val itemToDec = MyCart.cart.find { cartModel ->
                        cartModel.name == it.title && cartModel.qnt==it.qty
                    }
                    if (itemToDec != null) {
                        MyCart.update(itemToDec.copy(qnt = itemToDec.qnt-1))
                    }

                    cartItems = MyCart.cart.toList()
                },
                onRemove = { removedOrderLine -> // The item passed from CustomerCard is OrderLineVM
                    val itemToRemove = MyCart.cart.find { cartModel ->
                        cartModel.name == removedOrderLine.title  && cartModel.qnt==removedOrderLine.qty
                    }
                    if (itemToRemove != null) {
                        MyCart.removeItem(itemToRemove)
                        cartItems = MyCart.cart.toList()
                    }
                },
            )
        } else {
            Text("Cart is empty")
        }

        ReusableButton(
            text = "Order",
            onClick = order
        )
    }
}
