package com.example.milkchequedemo.utils

import com.example.milkchequedemo.presentation.screens.CustomerOrderVM
import com.example.milkchequedemo.presentation.screens.TotalsVM


fun calculateTotals(
    customers: List<CustomerOrderVM>,
    servicePct: Int,
    taxPct: Int
): TotalsVM {
    val subtotal = customers
        .filter { it.isSelected }
        .flatMap { it.lines }
        .sumOf { line ->
            val price = line.priceText
                .removeSuffix(" L.E.")
                .replace(",", "")
                .toDoubleOrNull() ?: 0.0
            price * line.qty
        }

    val service = subtotal * servicePct / 100
    val tax = subtotal * taxPct / 100
    val grand = subtotal + service + tax

    return TotalsVM(
        serviceText = "%.2f L.E.".format(service),
        taxText = "%.2f L.E.".format(tax),
        grandText = "%.2f L.E.".format(grand)
    )
}
