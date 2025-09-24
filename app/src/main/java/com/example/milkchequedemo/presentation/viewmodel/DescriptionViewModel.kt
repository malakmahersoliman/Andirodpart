package com.example.milkchequedemo.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milkchequedemo.R
import com.example.milkchequedemo.domain.model.MyCart
import com.example.milkchequedemo.domain.model.Session
import com.example.milkchequedemo.domain.model.SessionData
import com.example.milkchequedemo.domain.model.SessionResponse
import com.example.milkchequedemo.domain.usecase.CreateSessionUseCase
import com.example.milkchequedemo.domain.usecase.GetMenuItemUseCase
import com.example.milkchequedemo.presentation.screens.AddOnUI
import com.example.milkchequedemo.presentation.screens.DescriptionUiState
import com.example.milkchequedemo.presentation.screens.SizeOptionUI
import com.example.milkchequedemo.utils.ResponseWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@HiltViewModel
class DescriptionViewModel @Inject constructor(
    private val getMenuItem: GetMenuItemUseCase,
    private val savedState: SavedStateHandle,
    private val sessionUseCase: CreateSessionUseCase
) : ViewModel() {

    // nav argument name must match in your route: "productId"
    private val productId: Long = runCatching {
        savedState.get<String>("productId")?.toLong()
    }.getOrNull() ?: 0L

    private val _session: MutableStateFlow<SessionResponse?> = MutableStateFlow(null)
            val session=_session
    private val _state = MutableStateFlow(DescriptionUiState(
        rating = "4.5",
    ))
    val state: StateFlow<DescriptionUiState> = _state

    private val currencyFmt by lazy {
        NumberFormat.getCurrencyInstance(Locale("ar","EG"))
    }

    private var basePrice = 0.0
    private var qty = 1

//    init { load() }

    private fun priceText(v: Double) =
        currencyFmt.format(v).replace("EGP", "L.E")

    private fun pickImage(name: String): Int {
        val n = name.lowercase()
        return when {
            "pizza" in n -> R.drawable.pizza
            "pasta" in n -> R.drawable.pasta
            "dessert" in n -> R.drawable.dessert
            else -> R.drawable.pizza
        }
    }

    private fun total() = basePrice * qty

    fun load(
        userName: String,
        mail: String,
        storeId: String,
        tableId: String
    ) = viewModelScope.launch {
        when (val r=sessionUseCase(
            Session(
                userName = userName, mail = mail, storeId = storeId, tableId = tableId
            )
        )) {
            is ResponseWrapper.Success -> {
                SessionData.name=userName
                val item = r.data ?: return@launch
                SessionData.sessionId = item.sessionId.toString()
                SessionData.customerId = item.customerId
                SessionData.token=item.token
                SessionData.email=mail
                _session.value=item
            }
            is ResponseWrapper.Error -> {
                // TODO: expose error to UI if you want
            }
            else -> Unit
        }
    }



    // -------- public handlers used by the screen --------
    fun onInc() {
        qty++
        _state.value = _state.value.copy(qty = qty, totalText = priceText(total()))
    }

    fun onDec() {
        if (qty > 1) qty--
        _state.value = _state.value.copy(qty = qty, totalText = priceText(total()))
    }

    fun onSelectSize(id: String) {
        basePrice = when (id) {
            "l" -> _state.value.sizes.firstOrNull { it.id == "r" }?.let { basePrice * 1.2 } ?: basePrice * 1.2
            else -> _state.value.sizes.firstOrNull { it.id == "l" }?.let { basePrice / 1.2 } ?: basePrice
        }
        _state.value = _state.value.copy(
            sizes = _state.value.sizes.map { it.copy(selected = it.id == id) },
            totalText = priceText(total())
        )
    }

    fun onToggleAddOn(id: String) {
        _state.value = _state.value.copy(
            addOns = _state.value.addOns.map {
                if (it.id == id) it.copy(checked = !it.checked) else it
            }
        )
        // Optional: change basePrice if you want add-ons to affect total
    }
}
