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
import kotlin.math.round

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
    fun pay(){
        viewModelScope.launch {
            val total = round(
                _state.value.customers.filter {
                    it.isCustomerSelected  //true
                }.sumOf { it ->
                    it.orderItems.sumOf { order ->
                        order.price * order.quantity
                    }
                }
            ) * 100
            payUseCase(
                amountCents = total.toInt(),
                merchantOrderId = "1",
                email = SessionData.email!!
            )

            when (val res = payUseCase(
                amountCents = total.toInt(),
                merchantOrderId = "merchantOrderId",
                email = SessionData.email!!
            )) {
                is ResponseWrapper.Success -> {
                    _state.value = _state.value.copy(url = res.data!!,isLoading = false,error=null)
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

}