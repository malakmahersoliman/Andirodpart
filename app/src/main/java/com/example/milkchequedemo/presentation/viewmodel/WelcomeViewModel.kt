package com.example.milkchequedemo.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milkchequedemo.domain.model.StoreInfo
import com.example.milkchequedemo.domain.usecase.GetStoreInfoUseCase
import com.example.milkchequedemo.utils.ResponseWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
//class WelcomeViewModel @Inject constructor(
//    private val getStoreInfoUseCase: GetStoreInfoUseCase,
//    savedStateHandle: SavedStateHandle
//) : ViewModel() {
//
//    private val _getInfoFlow = MutableStateFlow<ResponseWrapper<StoreInfo>>(ResponseWrapper.Loading)
//    val getInfoFlow = _getInfoFlow
//
//    init {
////        getStoreInfo(storeId, tableId)      // fire immediately
//    }
//
//    fun getStoreInfo(storeId: Int, tableId: Int) = viewModelScope.launch {
//        // (No need for Dispatchers.IO: Retrofit suspend already off main)
//        getStoreInfoUseCase(storeId, tableId).collect { response ->
//            _getInfoFlow.update { response }
//        }
//    }
//}

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val getStoreInfo: GetStoreInfoUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<ResponseWrapper<StoreInfo>>(ResponseWrapper.Idle)
    val state = _state

    fun load(storeId: Int, tableId: Int) = viewModelScope.launch {
        getStoreInfo(storeId, tableId).collect { _state.value = it }
    }
}
