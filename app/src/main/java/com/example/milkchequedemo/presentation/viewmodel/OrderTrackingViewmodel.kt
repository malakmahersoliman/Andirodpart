package com.example.milkchequedemo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milkchequedemo.domain.model.SessionData
import com.example.milkchequedemo.domain.usecase.GetAllOrdersUseCase
import com.example.milkchequedemo.domain.usecase.PayUseCase
import com.example.milkchequedemo.presentation.screens.OrderTrackingUiState
import com.example.milkchequedemo.utils.ResponseWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class OrderTrackingViewmodel @Inject constructor(
    private val getAllOrdersUseCase: GetAllOrdersUseCase,
    private val payUseCase: PayUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OrderTrackingUiState())
    val state: StateFlow<OrderTrackingUiState> = _state

    init {
        load(SessionData.sessionId!!)
    }
    fun updateCustomer(index:Int,isSelected:Boolean){
        val list=_state.value.customers.toMutableList()
        list[index]=list[index].copy(
            isCustomerSelected = isSelected
        )
        _state.update {
            _state.value.copy(
                customers = list.toMutableList()
            )
        }
    }

    fun pay() {
        viewModelScope.launch {
            val orderId = SessionData.orderId?.toString()
            if (orderId == null) {
                _state.value = _state.value.copy(error = "Order ID is missing.", isLoading = false)
                return@launch
            }

            val totalSelected = _state.value.customers
                .filter { it.isCustomerSelected && it.orderItems.isNotEmpty() }
                .sumOf { c -> c.orderItems.sumOf { it.price * it.quantity } }

            val amountCents = kotlin.math.round(totalSelected * 100).toInt()

            _state.value = _state.value.copy(isLoading = true, error = null)

            val others = emptyList<Int>()

            when (val res = payUseCase(
                amountCents = amountCents,
                merchantOrderId = orderId,
                otherMerchantsOrderId = others
            )) {
                is ResponseWrapper.Success -> {
                    _state.value = _state.value.copy(url = res.data.orEmpty(),  isLoading = false, error = null)
                }
                is ResponseWrapper.Error -> {
                    _state.value = _state.value.copy(error = res.message, isLoading = false)
                }
                is ResponseWrapper.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
                ResponseWrapper.Idle -> {
                    _state.value = _state.value.copy(error = null, isLoading = false)
                }
            }
        }
    }
    fun load(sessionId: String) = viewModelScope.launch {
        when (val res = getAllOrdersUseCase(sessionId)) {
            is ResponseWrapper.Success -> {
                _state.value = _state.value.copy(customers = res.data!!,isLoading = false,error=null)
            }
            is ResponseWrapper.Error -> {
                _state.value = _state.value.copy(error = res.message,isLoading = false)
            }
            is ResponseWrapper.Loading ->{
                _state.value = _state.value.copy(isLoading = true)
            }
            ResponseWrapper.Idle ->{
                _state.value = _state.value.copy(error = null,isLoading = false)
            }
        }
    }
    fun consumePaymentUrl() {
        _state.value = _state.value.copy(url = null)
    }
}