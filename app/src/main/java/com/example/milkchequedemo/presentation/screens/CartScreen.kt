package com.example.milkchequedemo.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.milkchequedemo.data.config.AppConfig
import com.example.milkchequedemo.data.dto.AllOrdersResponse
import com.example.milkchequedemo.domain.model.MyCart
import com.example.milkchequedemo.domain.model.SessionData
import com.example.milkchequedemo.presentation.components.ErrorHandler
import com.example.milkchequedemo.presentation.components.LoadingIndicator
import com.example.milkchequedemo.presentation.components.ReusableButton
import com.example.milkchequedemo.presentation.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    order: () -> Unit,
    viewModel: CartViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Show loading state
    if (uiState.isLoading) {
        LoadingIndicator(message = "Loading cart...")
        return
    }
    
    // Show error state
    if (uiState.errorMessage != null) {
        ErrorHandler(
            errorMessage = uiState.errorMessage!!,
            onRetry = { viewModel.loadCartItems() }
        )
        return
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Your Cart") }
            )
        },
        bottomBar = {
            if (uiState.cartItems.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppConfig.UI.PADDING_MEDIUM.dp)
                ) {
                    OrderSummaryRow(
                        label = "Total", 
                        value = uiState.calculations.grandTotalText, 
                        emphasize = true
                    )
                    Spacer(modifier = Modifier.padding(top = AppConfig.UI.PADDING_SMALL.dp))
                    ReusableButton(
                        text = "Order (${uiState.cartItems.sumOf { it.qnt }} items)",
                        onClick = {
                            viewModel.placeOrder()
                            order()
                        },
                        isLoading = uiState.isPlacingOrder
                    )
                }
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (uiState.cartItems.isNotEmpty()) {
                // Customer block + lines list
                CustomerCard(
                    containEdit = true,
                   onInc = { line ->
                        val item = uiState.cartItems.find { it.name == line.title }
                        item?.let { viewModel.increaseQuantity(it) }
                    },
                    onDec = { line ->
                        val item = uiState.cartItems.find { it.name == line.title }
                        item?.let { viewModel.decreaseQuantity(it) }
                    },
                    onRemove = { removed ->
                        val item = uiState.cartItems.find { it.name == removed.title }
                        item?.let { viewModel.removeItem(it) }
                    },
                    onToggle = {}, onSeeAll = {},
                    customer = AllOrdersResponse(
                        customerId = SessionData.customerId!!,
                        customerName = SessionData.name?:"",
                        orderItems = uiState.cartItems.map{
                            AllOrdersResponse.OrderItem(
                                id = it.id.toInt(),
                                name = it.name,
                                price = it.price,
                                quantity = it.qnt
                            )
                        }
                    )
                )

                // Totals breakdown card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppConfig.UI.PADDING_MEDIUM.dp, vertical = 12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = AppConfig.UI.CARD_ELEVATION.dp)
                ) {
                    Column(Modifier.padding(AppConfig.UI.PADDING_MEDIUM.dp)) {
                        Text("Summary", fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.padding(top = AppConfig.UI.PADDING_SMALL.dp))
                        OrderSummaryRow("Subtotal", uiState.calculations.subtotalText)
                        OrderSummaryRow("Service (${AppConfig.Pricing.DEFAULT_SERVICE_PERCENTAGE}%)", uiState.calculations.serviceText)
                        OrderSummaryRow("Tax (${AppConfig.Pricing.DEFAULT_TAX_PERCENTAGE}%)", uiState.calculations.taxText)
                        Divider(Modifier.padding(vertical = AppConfig.UI.PADDING_SMALL.dp))
                        OrderSummaryRow("Grand Total", uiState.calculations.grandTotalText, emphasize = true)
                    }
                }
                Spacer(Modifier.weight(1f)) // push content above bottom bar
            } else {
                // Enhanced empty state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(AppConfig.UI.PADDING_LARGE.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your cart is empty",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.padding(top = AppConfig.UI.PADDING_SMALL.dp))
                    Text(
                        text = "Add some delicious items to get started!",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderSummaryRow(
    label: String,
    value: String,
    emphasize: Boolean = false
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = if (emphasize) FontWeight.SemiBold else FontWeight.Normal)
        Text(value, fontWeight = if (emphasize) FontWeight.SemiBold else FontWeight.Normal)
    }
}

private fun formatLE(amount: Double): String = String.format("%.2f L.E.", amount)
