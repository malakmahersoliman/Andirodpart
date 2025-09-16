package com.example.milkchequedemo.utils

import com.example.milkchequedemo.data.config.AppConfig
import com.example.milkchequedemo.domain.model.CartModel
import java.text.NumberFormat
import java.util.Locale

object CalculationUtils {
    
    private val currencyFormatter by lazy {
        NumberFormat.getCurrencyInstance(Locale(AppConfig.Currency.LOCALE_LANGUAGE, AppConfig.Currency.LOCALE_COUNTRY))
    }
    
    /**
     * Calculate subtotal from cart items with validation
     */
    fun calculateSubtotal(cartItems: List<CartModel>): Double {
        if (cartItems.isEmpty()) return 0.0
        
        return cartItems.sumOf { item ->
            validatePrice(item.price) * validateQuantity(item.qnt)
        }
    }
    
    /**
     * Calculate service charge with validation
     */
    fun calculateServiceCharge(subtotal: Double, servicePercentage: Int): Double {
        val validatedPercentage = validateServicePercentage(servicePercentage)
        return subtotal * (validatedPercentage / 100.0)
    }
    
    /**
     * Calculate tax with validation
     */
    fun calculateTax(subtotal: Double, taxPercentage: Int): Double {
        val validatedPercentage = validateTaxPercentage(taxPercentage)
        return subtotal * (validatedPercentage / 100.0)
    }
    
    /**
     * Calculate grand total including service and tax
     */
    fun calculateGrandTotal(subtotal: Double, serviceCharge: Double, tax: Double): Double {
        return subtotal + serviceCharge + tax
    }
    
    /**
     * Format currency value to Egyptian L.E. format
     */
    fun formatCurrency(amount: Double): String {
        return currencyFormatter.format(amount).replace("EGP", AppConfig.Currency.CURRENCY_SYMBOL)
    }
    
    /**
     * Complete calculation result for cart items
     */
    data class CalculationResult(
        val subtotal: Double,
        val serviceCharge: Double,
        val tax: Double,
        val grandTotal: Double,
        val subtotalText: String,
        val serviceText: String,
        val taxText: String,
        val grandTotalText: String,
        val isValid: Boolean = true,
        val errorMessage: String? = null
    )
    
    /**
     * Calculate all totals for cart items with given percentages
     */
    fun calculateAllTotals(
        cartItems: List<CartModel>,
        servicePercentage: Int = AppConfig.Pricing.DEFAULT_SERVICE_PERCENTAGE,
        taxPercentage: Int = AppConfig.Pricing.DEFAULT_TAX_PERCENTAGE
    ): CalculationResult {
        return try {
            val subtotal = calculateSubtotal(cartItems)
            val serviceCharge = calculateServiceCharge(subtotal, servicePercentage)
            val tax = calculateTax(subtotal, taxPercentage)
            val grandTotal = calculateGrandTotal(subtotal, serviceCharge, tax)
            
            CalculationResult(
                subtotal = subtotal,
                serviceCharge = serviceCharge,
                tax = tax,
                grandTotal = grandTotal,
                subtotalText = formatCurrency(subtotal),
                serviceText = formatCurrency(serviceCharge),
                taxText = formatCurrency(tax),
                grandTotalText = formatCurrency(grandTotal),
                isValid = true
            )
        } catch (e: Exception) {
            CalculationResult(
                subtotal = 0.0,
                serviceCharge = 0.0,
                tax = 0.0,
                grandTotal = 0.0,
                subtotalText = formatCurrency(0.0),
                serviceText = formatCurrency(0.0),
                taxText = formatCurrency(0.0),
                grandTotalText = formatCurrency(0.0),
                isValid = false,
                errorMessage = e.message
            )
        }
    }
    
    // Validation functions
    private fun validatePrice(price: Double): Double {
        return if (price >= 0) price else throw IllegalArgumentException("Price cannot be negative")
    }
    
    private fun validateQuantity(quantity: Int): Int {
        return if (quantity in AppConfig.Cart.MIN_QUANTITY_PER_ITEM..AppConfig.Cart.MAX_QUANTITY_PER_ITEM) {
            quantity
        } else {
            throw IllegalArgumentException("Quantity must be between ${AppConfig.Cart.MIN_QUANTITY_PER_ITEM} and ${AppConfig.Cart.MAX_QUANTITY_PER_ITEM}")
        }
    }
    
    private fun validateServicePercentage(percentage: Int): Int {
        return if (percentage in AppConfig.Pricing.MIN_SERVICE_PERCENTAGE..AppConfig.Pricing.MAX_SERVICE_PERCENTAGE) {
            percentage
        } else {
            AppConfig.Pricing.DEFAULT_SERVICE_PERCENTAGE
        }
    }
    
    private fun validateTaxPercentage(percentage: Int): Int {
        return if (percentage in AppConfig.Pricing.MIN_TAX_PERCENTAGE..AppConfig.Pricing.MAX_TAX_PERCENTAGE) {
            percentage
        } else {
            AppConfig.Pricing.DEFAULT_TAX_PERCENTAGE
        }
    }
}
