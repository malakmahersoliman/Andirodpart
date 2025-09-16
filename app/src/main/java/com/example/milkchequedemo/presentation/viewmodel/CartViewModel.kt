package com.example.milkchequedemo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milkchequedemo.data.config.AppConfig
import com.example.milkchequedemo.domain.model.CartModel
import com.example.milkchequedemo.domain.model.MyCart
import com.example.milkchequedemo.utils.CalculationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()
    
    init {
        loadCartItems()
    }
    
    fun loadCartItems() {
        viewModelScope.launch {
            val cartItems = MyCart.cart.toList()
            val calculations = CalculationUtils.calculateAllTotals(
                cartItems = cartItems,
                servicePercentage = AppConfig.Pricing.DEFAULT_SERVICE_PERCENTAGE,
                taxPercentage = AppConfig.Pricing.DEFAULT_TAX_PERCENTAGE
            )
            
            _uiState.value = _uiState.value.copy(
                cartItems = cartItems,
                calculations = calculations,
                isLoading = false
            )
        }
    }
    
    fun increaseQuantity(item: CartModel) {
        viewModelScope.launch {
            if (item.qnt < AppConfig.Cart.MAX_QUANTITY_PER_ITEM) {
                MyCart.update(item.copy(qnt = item.qnt + 1))
                loadCartItems()
            }
        }
    }
    
    fun decreaseQuantity(item: CartModel) {
        viewModelScope.launch {
            val newQuantity = item.qnt - 1
            if (newQuantity <= 0) {
                MyCart.removeItem(item)
            } else {
                MyCart.update(item.copy(qnt = newQuantity))
            }
            loadCartItems()
        }
    }
    
    fun removeItem(item: CartModel) {
        viewModelScope.launch {
            MyCart.removeItem(item)
            loadCartItems()
        }
    }
    
    fun clearCart() {
        viewModelScope.launch {
            MyCart.clear()
            loadCartItems()
        }
    }
    
    fun placeOrder() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isPlacingOrder = true)
            // TODO: Implement actual order placement logic
            // For now, just simulate
            kotlinx.coroutines.delay(2000)
            _uiState.value = _uiState.value.copy(isPlacingOrder = false)
        }
    }
}

data class CartUiState(
    val cartItems: List<CartModel> = emptyList(),
    val calculations: CalculationUtils.CalculationResult = CalculationUtils.CalculationResult(
        subtotal = 0.0,
        serviceCharge = 0.0,
        tax = 0.0,
        grandTotal = 0.0,
        subtotalText = "0.00 L.E.",
        serviceText = "0.00 L.E.",
        taxText = "0.00 L.E.",
        grandTotalText = "0.00 L.E."
    ),
    val isLoading: Boolean = true,
    val isPlacingOrder: Boolean = false,
    val errorMessage: String? = null
)
