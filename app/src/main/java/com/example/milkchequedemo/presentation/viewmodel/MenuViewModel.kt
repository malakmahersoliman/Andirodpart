package com.example.milkchequedemo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milkchequedemo.R
import com.example.milkchequedemo.domain.model.MenuItem
import com.example.milkchequedemo.domain.usecase.GetMenuUseCase
import com.example.milkchequedemo.presentation.screens.MenuUiState
import com.example.milkchequedemo.presentation.screens.ProductUI
import com.example.milkchequedemo.utils.ResponseWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getMenu: GetMenuUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state

    private val currencyFmt by lazy {
        NumberFormat.getCurrencyInstance(Locale("ar","EG"))
    }

    fun load(storeId: Int,
                     tableId: Int) = viewModelScope.launch {
        when (val res = getMenu(storeId = storeId, tableId = tableId)) {
            is ResponseWrapper.Success -> {
                _state.value = _state.value.copy(bestSellers = res.data!!,isLoading = false,error=null)
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

    private fun MenuItem.toProductUI(): ProductUI =
        ProductUI(
            id = id.toString(),
            imageRes = guessLocalImage(name),
            title = name,
            priceText = currencyFmt.format(price).replace("EGP", "L.E")
        )

    private fun guessLocalImage(name: String): Int {
        val n = name.lowercase()
        return when {
            "pizza" in n -> R.drawable.pizza
            "pasta" in n -> R.drawable.pasta
            "dessert" in n -> R.drawable.dessert
            "cola" in n || "drink" in n -> R.drawable.coka
            else -> R.drawable.pizza // fallback
        }
    }
}
