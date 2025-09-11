package com.example.milkchequedemo.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milkchequedemo.presentation.components.ReusableButton
import com.example.milkchequedemo.ui.theme.MexicanRed

/* ---------- UI State models (VM-friendly) ---------- */
data class OrderTrackingUiState(
    val tableLabel: String = "Order Table 4",
    val customers: List<CustomerOrderVM> = emptyList(),
    val servicePct: Int = 10,
    val taxPct: Int = 5,
    val totals: TotalsVM = TotalsVM(),
    val isPlacing: Boolean = false
)
data class CustomerOrderVM(
    val id: String,
    val name: String,
    val isSelected: Boolean,
    val lines: List<OrderLineVM>,
    val totalText: String
)
data class OrderLineVM(
    val id: String,
    val title: String,
    val priceText: String,
    val qty: Int
)
data class TotalsVM(
    val serviceText: String = "0.00 L.E.",
    val taxText: String = "0.00 L.E.",
    val grandText: String = "0.00 L.E."
)

/* ----------------------------- Screen -------------

//todo navigate for payment ---------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(
    state: OrderTrackingUiState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onToggleCustomer: (customerId: String, checked: Boolean) -> Unit = { _, _ -> },
    onSeeAll: (customerId: String) -> Unit = { _ -> },
    onInc: (item:OrderLineVM?) -> Unit = {  },
    onDec: (item:OrderLineVM?) -> Unit = { },
    onRemoveLine: (customerId: String, lineId: String) -> Unit = { _, _ -> },
    onPlaceOrder: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = state.tableLabel,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(Color(0xFFF7F7F7))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                TotalsSection(
                    servicePct = state.servicePct,
                    taxPct = state.taxPct,
                    totals = state.totals
                )
                Spacer(Modifier.height(12.dp))
                ReusableButton(
                    text = "Order",
                    onClick = onPlaceOrder,
                    isLoading = state.isPlacing,
                    height = 52.dp,
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }
    ) { inner ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssistChip(
                    onClick = { /* hook later */ },
                    label = { Text("All") },
                    leadingIcon = {
                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 140.dp)
            ) {
                items(state.customers, key = { it.id }) { customer ->
                    CustomerCard(
                        customer = customer,
                        onToggle = { checked -> onToggleCustomer(customer.id, checked) },
                        onSeeAll = { onSeeAll(customer.id) },
                        onInc = onInc,
                        onDec = onDec,
                        onRemove = { lineId -> onRemoveLine(customer.id, "") }
                    )
                }
            }
        }
    }
}

/* --------------------------- Components --------------------------- */
@Composable
fun CustomerCard(
    customer: CustomerOrderVM,
    onToggle: (Boolean) -> Unit,
    onSeeAll: () -> Unit,
    onInc: (item:OrderLineVM) -> Unit,
    onDec: (item:OrderLineVM) -> Unit,
    onRemove: (item:OrderLineVM) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.2.dp, MexicanRed.copy(alpha = 0.8f)),
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Checkbox(checked = customer.isSelected, onCheckedChange = onToggle, modifier = Modifier.size(22.dp))
                Text(
                    text = customer.name,
                    style = MaterialTheme.typography.titleMedium.copy(color = MexicanRed, fontWeight = FontWeight.SemiBold)
                )
                Spacer(Modifier.weight(1f))
                TextButton(onClick = onSeeAll, contentPadding = PaddingValues(0.dp)) {
                    Text("See All", fontSize = 12.sp)
                }
                IconButton(onClick = { /* clear all later */ }, modifier = Modifier.size(22.dp)) {
                    Icon(Icons.Filled.Delete, contentDescription = "Clear", tint = Color.DarkGray)
                }
            }

            Spacer(Modifier.height(6.dp))

            customer.lines.forEach { line ->
                OrderLineRow(
                    line = line,
                    onInc = { onInc(line) },
                    onDec = { onDec(line) },
                    onRemove = { onRemove(line) }
                )
                Spacer(Modifier.height(4.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold))
                Spacer(Modifier.width(8.dp))
                Text("(included Tax & Service)", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                Spacer(Modifier.weight(1f))
                Text(
                    customer.totalText,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MexicanRed, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun OrderLineRow(
    line: OrderLineVM,
    onInc: () -> Unit,
    onDec: () -> Unit,
    onRemove: () -> Unit,
    height: Dp = 28.dp
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = line.title, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(text = line.priceText, modifier = Modifier.padding(end = 10.dp), style = MaterialTheme.typography.bodyMedium)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(100))
                .background(Color(0xFFF2F2F2))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            IconButton(onClick = {
                onRemove()
            }, modifier = Modifier.size(height)) {
                Icon(Icons.Filled.Delete, contentDescription = "Remove item", tint = Color.DarkGray)
            }
            Stepper(qty = line.qty, onInc = onInc, onDec = onDec, height = height)
        }
    }
}

@Composable
private fun Stepper(qty: Int, onInc: () -> Unit, onDec: () -> Unit, height: Dp) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clip(RoundedCornerShape(100)).background(Color.White)
    ) {
        IconButton(onClick = onDec, modifier = Modifier.size(height)) { Icon(Icons.Filled.Remove, contentDescription = "Decrease") }
        Box(modifier = Modifier.height(height).widthIn(min = 24.dp), contentAlignment = Alignment.Center) {
            Text("$qty", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
        IconButton(onClick = onInc, modifier = Modifier.size(height)) { Icon(Icons.Filled.Add, contentDescription = "Increase") }
    }
}

/* ---- Totals: fix was making helpers @Composable instead of plain fun ---- */
@Composable
private fun TotalsSection(servicePct: Int, taxPct: Int, totals: TotalsVM) {
    TotalLinePill(
        label = "Service${servicePct}%",
        value = totals.serviceText,
        bg = Color(0xFFEAF8F1),
        textColor = Color(0xFF1F8F5B)
    )
    Spacer(Modifier.height(8.dp))
    TotalLinePill(
        label = "Tax  ${taxPct}%",
        value = totals.taxText,
        bg = Color(0xFFEAF8F1),
        textColor = Color(0xFF1F8F5B)
    )
    Spacer(Modifier.height(8.dp))
    TotalGrandLine(label = "Total", value = totals.grandText)
}

@Composable
private fun TotalLinePill(label: String, value: String, bg: Color, textColor: Color) {
    Surface(color = bg, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(label, color = textColor)
            Spacer(Modifier.weight(1f))
            Text(value, color = textColor, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun TotalGrandLine(label: String, value: String) {
    Surface(color = Color(0xFFE9F8EE), shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(label, fontWeight = FontWeight.SemiBold, color = Color(0xFF0E7A4E))
            Spacer(Modifier.weight(1f))
            Text(value, fontWeight = FontWeight.Bold, color = Color(0xFF0E7A4E))
        }
    }
}

/* ------------------------------ Preview ------------------------------ */
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun OrderTrackingPreview() {
    val state = OrderTrackingUiState(
        tableLabel = "Order Table 4",
        customers = listOf(
            CustomerOrderVM(
                id = "c1",
                name = "Malak",
                isSelected = true,
                lines = listOf(
                    OrderLineVM("l1", "Cheese Fries", "60.00 L.E.", 1),
                    OrderLineVM("l2", "Pepsi", "25.00 L.E.", 1),
                    OrderLineVM("l3", "Buffalo Chicken", "478.00 L.E.", 2)
                ),
                totalText = "580.99 L.E."
            ),
            CustomerOrderVM(
                id = "c2",
                name = "Rola",
                isSelected = true,
                lines = listOf(
                    OrderLineVM("l4", "Penne all'Arrabbiata", "258.00 L.E.", 1),
                    OrderLineVM("l5", "Coke Diet", "25.00 L.E.", 2),
                    OrderLineVM("l6", "Polish doughnuts", "85.99 L.E.", 2)
                ),
                totalText = "368.99 L.E."
            ),
            CustomerOrderVM(
                id = "c3",
                name = "Lulu",
                isSelected = false,
                lines = listOf(OrderLineVM("l7", "Melting Cheese Pizza", "407.4 L.E.", 1)),
                totalText = "425.03 L.E."
            ),
            CustomerOrderVM(
                id = "c4",
                name = "Hamed",
                isSelected = true,
                lines = listOf(
                    OrderLineVM("l8", "Cheese Fries", "60.00 L.E.", 1),
                    OrderLineVM("l9", "Pepsi", "25.00 L.E.", 1)
                ),
                totalText = "102.99 L.E."
            ),
            CustomerOrderVM(
                id = "c5",
                name = "Youssef",
                isSelected = true,
                lines = listOf(
                    OrderLineVM("l10", "French Fries", "30.00 L.E.", 1),
                    OrderLineVM("l11", "Pepsi", "25.00 L.E.", 1),
                    OrderLineVM("l12", "Shrimp Scampi", "300.76 L.E.", 2)
                ),
                totalText = "355.76 L.E."
            )
        ),
        servicePct = 10,
        taxPct = 5,
        totals = TotalsVM(serviceText = "10.99 L.E.", taxText = "7.00 L.E.", grandText = "1,833.76 L.E."),
        isPlacing = false
    )

    OrderTrackingScreen(state = state)
}
