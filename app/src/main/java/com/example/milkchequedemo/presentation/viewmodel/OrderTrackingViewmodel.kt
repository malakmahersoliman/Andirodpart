package com.example.milkchequedemo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milkchequedemo.domain.model.SessionData
import com.example.milkchequedemo.domain.usecase.GetAllOrdersUseCase
import com.example.milkchequedemo.presentation.screens.MenuUiState
import com.example.milkchequedemo.presentation.screens.OrderTrackingUiState
import com.example.milkchequedemo.utils.ResponseWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OrderTrackingViewmodel @Inject constructor(
    private val getAllOrdersUseCase: GetAllOrdersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OrderTrackingUiState())
    val state: StateFlow<OrderTrackingUiState> = _state

    init {
        load(SessionData.sessionId!!)
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