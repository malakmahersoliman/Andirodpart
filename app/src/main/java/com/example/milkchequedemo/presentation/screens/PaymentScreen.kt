package com.example.milkchequedemo.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milkchequedemo.ui.theme.MexicanRed

/* ----------------------------- UI State ----------------------------- */

enum class PayMode { Individually, All }
enum class PayMethod { Cash, Credit }

data class PayerVM(
    val id: String,
    val name: String,
    val totalText: String
)

data class PaymentUiState(
    val title: String = "Payment",
    val tableLabel: String = "Table #4",
    val mode: PayMode = PayMode.Individually,
    val payers: List<PayerVM> = emptyList(),
    val grandTotalText: String = "0.00 L.E."
)

/* ----------------------------- Screen ------------------------------ */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    state: PaymentUiState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onModeChange: (PayMode) -> Unit = {},
    onPayPerson: (payerId: String, method: PayMethod) -> Unit = { _, _ -> },
    onPayAll: (method: PayMethod) -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            state.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            state.tableLabel,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(color = Color(0xFFF7F7F7)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Total pill
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp)),
                        color = Color(0xFFEFF7F2)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total", color = Color(0xFF0E7A4E), fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.weight(1f))
                            Text(
                                state.grandTotalText,
                                color = Color(0xFF0E7A4E),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Pay button only in "All" mode
                    if (state.mode == PayMode.All) {
                        Spacer(Modifier.width(12.dp))
                        PayMenuButton(onPay = onPayAll)
                    }
                }
            }
        }
    ) { inner ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(inner)
        ) {
//            SegmentedTwoOption(
//                left = "Individually",
//                right = "All",
//                selectedLeft = state.mode == PayMode.Individually,
//                onLeft = { onModeChange(PayMode.Individually) },
//                onRight = { onModeChange(PayMode.All) },
//                modifier = Modifier
//                    .padding(horizontal = 16.dp)
//                    .padding(top = 8.dp, bottom = 12.dp)
//            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                if (state.mode == PayMode.Individually) {
                    items(state.payers, key = { it.id }) { p ->
                        PayerCard(
                            payer = p,
                            onPay = { method -> onPayPerson(p.id, method) }
                        )
                    }
                } else {
                    // All mode: no extra pay button in list; bottom bar handles Pay.
                    item { Spacer(Modifier.height(8.dp)) }
                    // If you want a summary card without a button, you can render it here.
                    // item {
                    //     AllSummaryCard(grandTotal = state.grandTotalText)
                    // }
                }
            }
        }
    }
}

/* --------------------------- Components ---------------------------- */

@Composable
private fun SegmentedTwoOption(
    left: String,
    right: String,
    selectedLeft: Boolean,
    onLeft: () -> Unit,
    onRight: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = Color(0xFFF0EFEF)
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = bg,
        shape = RoundedCornerShape(26.dp)
    ) {
        Row(Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
            SegmentedChip(
                text = left,
                selected = selectedLeft,
                onClick = onLeft,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(4.dp))
            SegmentedChip(
                text = right,
                selected = !selectedLeft,
                onClick = onRight,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SegmentedChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val container = if (selected) MexicanRed else Color.Transparent
    val content = if (selected) Color.White else Color(0xFF6B6B6B)

    Surface(
        modifier = modifier.height(40.dp),
        color = container,
        shape = RoundedCornerShape(22.dp),
        border = if (selected) null else BorderStroke(0.dp, Color.Transparent),
        onClick = onClick
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text, color = content, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun PayerCard(
    payer: PayerVM,
    onPay: (PayMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.2.dp, MexicanRed.copy(alpha = 0.85f)),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(payer.name, color = MexicanRed, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Total", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.width(8.dp))
                    Text("(included Tax & Service)", color = Color.Gray, fontSize = 12.sp)
                    Spacer(Modifier.weight(1f))
                    Text(payer.totalText, fontWeight = FontWeight.Bold)
                }
            }
            PayMenuButton(onPay = onPay)
        }
    }
}

@Composable
private fun AllPayCard(
    grandTotal: String,
    onPay: (PayMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    // Kept for reference; not used when Pay button is in bottom bar.
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.2.dp, MexicanRed.copy(alpha = 0.85f))
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text("All Customers", color = MexicanRed, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Total", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.width(8.dp))
                    Text("(included Tax & Service)", color = Color.Gray, fontSize = 12.sp)
                    Spacer(Modifier.weight(1f))
                    Text(grandTotal, fontWeight = FontWeight.Bold)
                }
            }
            PayMenuButton(onPay = onPay)
        }
    }
}

/** Red “Pay ▽” button with dropdown for Cash / Credit */
@Composable
private fun PayMenuButton(
    onPay: (PayMethod) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = MexicanRed),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text("Pay", color = Color.White, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("Cash", fontWeight = FontWeight.SemiBold) },
                onClick = { expanded = false; onPay(PayMethod.Cash) }
            )
            DropdownMenuItem(
                text = { Text("Credit", fontWeight = FontWeight.SemiBold) },
                onClick = { expanded = false; onPay(PayMethod.Credit) }
            )
        }
    }
}

/* ------------------------------ Preview ----------------------------- */

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PaymentPreview() {
    val fake = PaymentUiState(
        tableLabel = "Table #4",
        mode = PayMode.All, // set to All so the Pay button shows in preview
        payers = listOf(
            PayerVM("p1", "Malak", "590.99 L.E."),
            PayerVM("p2", "Rola", "368.99 L.E."),
            PayerVM("p3", "Lulu", "425.03 L.E."),
            PayerVM("p4", "Hamed", "102.99 L.E."),
            PayerVM("p5", "Youssef", "355.76 L.E.")
        ),
        grandTotalText = "1,896.99 L.E."
    )
    PaymentScreen(
        state = fake,
        onBack = {},
        onModeChange = {},
        onPayPerson = { _, _ -> },
        onPayAll = {}
    )
}
