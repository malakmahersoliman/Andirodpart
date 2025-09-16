package com.example.milkchequedemo.data.config

/**
 * Application configuration constants
 * Centralizes all configurable values for easy maintenance
 */
object AppConfig {
    
    // Tax and Service Configuration
    object Pricing {
        const val DEFAULT_SERVICE_PERCENTAGE = 10
        const val DEFAULT_TAX_PERCENTAGE = 5
        const val MIN_SERVICE_PERCENTAGE = 0
        const val MAX_SERVICE_PERCENTAGE = 50
        const val MIN_TAX_PERCENTAGE = 0
        const val MAX_TAX_PERCENTAGE = 30
    }
    
    // Currency Configuration
    object Currency {
        const val CURRENCY_SYMBOL = "L.E."
        const val CURRENCY_CODE = "EGP"
        const val DECIMAL_PLACES = 2
        const val LOCALE_LANGUAGE = "ar"
        const val LOCALE_COUNTRY = "EG"
    }
    
    // UI Configuration
    object UI {
        const val DEFAULT_ANIMATION_DURATION = 300
        const val CARD_ELEVATION = 2
        const val BORDER_RADIUS = 12
        const val PADDING_SMALL = 8
        const val PADDING_MEDIUM = 16
        const val PADDING_LARGE = 24
    }
    
    // Cart Configuration
    object Cart {
        const val MAX_QUANTITY_PER_ITEM = 99
        const val MIN_QUANTITY_PER_ITEM = 1
        const val MAX_ITEMS_IN_CART = 50
    }
    
    // Network Configuration
    object Network {
        const val CONNECT_TIMEOUT_SECONDS = 30
        const val READ_TIMEOUT_SECONDS = 30
        const val WRITE_TIMEOUT_SECONDS = 30
    }
    
    // Validation Configuration
    object Validation {
        const val MIN_NAME_LENGTH = 2
        const val MAX_NAME_LENGTH = 50
        const val PHONE_NUMBER_LENGTH = 11
        const val MIN_PASSWORD_LENGTH = 6
    }
}
