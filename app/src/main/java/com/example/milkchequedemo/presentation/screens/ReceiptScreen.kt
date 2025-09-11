package com.example.milkchequedemo.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milkchequedemo.ui.theme.MexicanRed

/* ========================= UI STATE (VM-friendly) ========================= */

data class ReceiptUiState(
    val address: String,
    val tel: String,
    val dateText: String,
    val timeText: String,
    val items: List<ReceiptItemVM>,
    val itemCountText: String,
    val totalText: String,
    val maskedCard: String,
    val authCode: String,
    val cardHolder: String,
    val barcodeSeed: String = authCode // used to draw a deterministic fake barcode
)

data class ReceiptItemVM(
    val qty: Int,
    val title: String,
    val amountText: String
)

/* ================================ SCREEN ================================= */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScreen(
    state: ReceiptUiState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onNext: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Receipt", fontWeight = FontWeight.Bold, color = MexicanRed) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onNext) {
                    Text("Next", color = MexicanRed, fontWeight = FontWeight.SemiBold)
                    Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MexicanRed)
                }
            }
        }
    ) { inner ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFFBFB),
                border = BorderStroke(1.2.dp, MexicanRed.copy(alpha = 0.8f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        "Receipt",
                        color = MexicanRed,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(Modifier.height(16.dp))

                    Text("Address : ${state.address}", fontWeight = FontWeight.SemiBold)
                    Text("Tel : ${state.tel}", fontWeight = FontWeight.SemiBold)

                    Spacer(Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Date : ${state.dateText}", fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.weight(1f))
                        Text(state.timeText, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(Modifier.height(10.dp))
                    DottedDivider()
                    Spacer(Modifier.height(10.dp))

                    // Header row
                    Row {
                        Text("Qty", modifier = Modifier.width(36.dp), fontWeight = FontWeight.SemiBold)
                        Text("ITEM", modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                        Text("AMT", modifier = Modifier.width(110.dp), textAlign = TextAlign.End, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(Modifier.height(8.dp))
                    DottedDivider()
                    Spacer(Modifier.height(6.dp))

                    // Items
                    state.items.forEachIndexed { idx, it ->
                        Row(modifier = Modifier.padding(vertical = 6.dp)) {
                            Text("${idx + 1}", modifier = Modifier.width(36.dp))
                            Text(it.title, modifier = Modifier.weight(1f))
                            Text(it.amountText, modifier = Modifier.width(110.dp), textAlign = TextAlign.End)
                        }
                    }

                    Spacer(Modifier.height(10.dp))
                    DottedDivider()
                    Spacer(Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Item Count", modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                        Text(state.itemCountText, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Total", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text(state.totalText, fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(10.dp))
                    DottedDivider()
                    Spacer(Modifier.height(12.dp))

                    Text("CARD #        ${state.maskedCard}", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(6.dp))
                    Row {
                        Text("Auth Code     ", fontWeight = FontWeight.SemiBold)
                        Text(state.authCode)
                    }
                    Spacer(Modifier.height(6.dp))
                    Row {
                        Text("CardHolder    ", fontWeight = FontWeight.SemiBold)
                        Text(state.cardHolder)
                    }

                    Spacer(Modifier.height(18.dp))

                    Text(
                        "THANK YOU FOR VISITING!",
                        color = MexicanRed,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(Modifier.height(12.dp))

                    // Fake barcode purely for UI (no lib, deterministic from seed)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .background(Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        FakeBarcode(seed = state.barcodeSeed, barColor = Color.Black)
                    }
                }
            }
        }
    }
}

/* ============================== COMPONENTS =============================== */

@Composable
private fun DottedDivider(
    color: Color = MexicanRed.copy(alpha = 0.7f)
) {
    val dash = remember { PathEffect.dashPathEffect(floatArrayOf(6f, 8f), 0f) }
    Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {
        drawLine(
            color = color,
            start = androidx.compose.ui.geometry.Offset(0f, 0f),
            end = androidx.compose.ui.geometry.Offset(size.width, 0f),
            strokeWidth = 2f,
            cap = Stroke.DefaultCap,
            pathEffect = dash
        )
    }
}

/** Very lightweight visual barcode (UI only). */
@Composable
private fun FakeBarcode(
    seed: String,
    barColor: Color,
) {
    Canvas(modifier = Modifier.fillMaxWidth().height(54.dp)) {
        // derive pseudo pattern from seed
        val bytes = seed.flatMap { listOf(it.code % 7 + 2, it.code % 5 + 1) }
        var x = 0f
        val height = size.height
        val totalUnits = bytes.sum().toFloat()
        val unitW = size.width / (totalUnits + 40f) // add margins

        // left quiet zone
        x += unitW * 20

        bytes.forEachIndexed { i, wUnits ->
            val w = wUnits * unitW
            if (i % 2 == 0) {
                // draw bar
                drawRect(
                    color = barColor,
                    topLeft = androidx.compose.ui.geometry.Offset(x, 0f),
                    size = androidx.compose.ui.geometry.Size(w, height)
                )
            }
            x += w
        }
        // right quiet zone implicitly left
    }
}

/* ================================= PREVIEW =============================== */

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ReceiptPreview() {
    val state = ReceiptUiState(
        address = "DownTown Cairo",
        tel = "123-456-7890",
        dateText = "01-01-2025",
        timeText = "3:35PM",
        items = listOf(
            ReceiptItemVM(1, "Cheese Fries", "60.00 L.E."),
            ReceiptItemVM(2, "Pepsi", "25.00 L.E."),
            ReceiptItemVM(3, "Buffalo Chicken", "478.00 L.E."),
            ReceiptItemVM(4, "Water", "10.00 L.E."),
            ReceiptItemVM(5, "Penne all'Arrabbiata", "350.00 L.E.")
        ),
        itemCountText = "5",
        totalText = "940.99 L.E.",
        maskedCard = "****  ****  ****  2025",
        authCode = "123421",
        cardHolder = "Malak Ahmed"
    )
    ReceiptScreen(state = state)
}
