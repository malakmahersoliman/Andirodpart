package com.example.milkchequedemo.presentation.screens

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milkchequedemo.R
import com.example.milkchequedemo.presentation.components.ReusableButton
import com.example.milkchequedemo.ui.theme.MexicanRed

/* ======================= UI STATE (view-only) ======================= */

data class DescriptionUiState(
    val title: String = "",
    val subtitle: String = "",          // e.g., "Pizza Italiano"
    val rating: String = "4.5",
    @DrawableRes val image: Int = R.drawable.pizza,
    val sizes: List<SizeOptionUI> = emptyList(),
    val addOns: List<AddOnUI> = emptyList(),
    val qty: Int = 1,
    val totalText: String = "0.00 L.E.",
    val canDelete: Boolean = true       // show trash icon near qty
)

data class SizeOptionUI(
    val id: String,
    val label: String,
    val priceText: String,              // "12.00 L.E."
    val selected: Boolean
)

data class AddOnUI(
    val id: String,
    val title: String,
    val sub: String,                    // e.g., "50 gm +5.40 L.E."
    @DrawableRes val image: Int,
    val checked: Boolean
)

/* ======================= SCREEN ======================= */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionScreen(
    state: DescriptionUiState,
    onBack: () -> Unit = {},
    onSelectSize: (id: String) -> Unit = {},
    onToggleAddOn: (id: String) -> Unit = {},
    onDecQty: () -> Unit = {},
    onIncQty: () -> Unit = {},
    onDelete: () -> Unit = {},
    onAddToCart: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {}
            )
        },
        bottomBar = {
            BottomBar(
                qty = state.qty,
                canDelete = state.canDelete,
                totalText = state.totalText,
                onDec = onDecQty, onInc = onIncQty,
                onDelete = onDelete,
                onAddToCart = onAddToCart
            )
        }
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 12.dp)
        ) {
            item {
                // Big image
                Image(
                    painter = painterResource(id = state.image),
                    contentDescription = state.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            item {
                // Title + meta
                Column {
                    Text(
                        state.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = state.subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.width(12.dp))
                        Text("★ ${state.rating}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Size options
            if (state.sizes.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        state.sizes.forEach { s ->
                            SizeOptionChip(
                                option = s,
                                onClick = { onSelectSize(s.id) },
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(Modifier.width(8.dp))
                        }
                    }
                }
            }

            // Add-ons
            if (state.addOns.isNotEmpty()) {
                item {
                    Text("Add Ingredients", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                items(state.addOns.size) { i ->
                    AddOnRow(
                        vm = state.addOns[i],
                        onToggle = { onToggleAddOn(state.addOns[i].id) }
                    )
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

/* ======================= PIECES ======================= */

@Composable
private fun SizeOptionChip(
    option: SizeOptionUI,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val border = if (option.selected) BorderStroke(2.dp, MexicanRed) else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    val dotColor = if (option.selected) MexicanRed else MaterialTheme.colorScheme.outline

    Surface(
        modifier = modifier.height(88.dp),
        shape = RoundedCornerShape(16.dp),
        border = border,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // radio-dot
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .border(BorderStroke(2.dp, dotColor), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (option.selected) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(MexicanRed)
                    )
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(option.label, fontSize = 13.sp)
            Text(option.priceText, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AddOnRow(
    vm: AddOnUI,
    onToggle: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFF6F6F6),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = vm.image),
                contentDescription = vm.title,
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(vm.title, fontWeight = FontWeight.SemiBold)
                Text(vm.sub, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            }
            SelectBox(
                checked = vm.checked,
                onClick = onToggle
            )
        }
    }
}

@Composable
private fun SelectBox(checked: Boolean, onClick: () -> Unit) {
    val border = if (checked) MexicanRed else MaterialTheme.colorScheme.outline
    val fill = if (checked) MexicanRed else Color.Transparent
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(RoundedCornerShape(6.dp))
            .border(BorderStroke(2.dp, border), RoundedCornerShape(6.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (checked) Box(Modifier.size(12.dp).clip(CircleShape).background(fill))
    }
}

@Composable
private fun BottomBar(
    qty: Int,
    canDelete: Boolean,
    totalText: String,
    onDec: () -> Unit,
    onInc: () -> Unit,
    onDelete: () -> Unit,
    onAddToCart: () -> Unit
) {
    Surface(
        tonalElevation = 2.dp,
        shadowElevation = 12.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .navigationBarsPadding(),               // ✅ avoid overlap with system bar
            verticalAlignment = Alignment.CenterVertically
        ) {

            // --- Stepper pill ---
            Surface(
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.5.dp, MexicanRed),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .height(48.dp)
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // trash (auto-dim when disabled)
                    IconButton(
                        onClick = onDelete,
                        enabled = canDelete,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Remove item",
                            tint = if (canDelete) MexicanRed
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // vertical divider
                    Box(
                        Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )

                    IconButton(onClick = onDec, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = MexicanRed)
                    }

                    // animated quantity
                    AnimatedContent(
                        targetState = qty,
                        label = "qty-anim"
                    ) { value ->
                        Text(
                            value.toString(),
                            modifier = Modifier.widthIn(min = 28.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    IconButton(onClick = onInc, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.Default.Add, contentDescription = "Increase", tint = MexicanRed)
                    }
                }
            }

            Spacer(Modifier.width(12.dp))

            // --- CTA button with two-line label (keeps total always visible) ---
            ReusableButton(
                text = "Add to Cart\n$totalText".lowercase(), // trick: stop uppercase from shouting
                onClick = onAddToCart,
                height = 52.dp,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/* ======================= PREVIEW ======================= */

@Preview(showBackground = true)
@Composable
private fun DescriptionPreview() {
    val s = DescriptionUiState(
        title = "Melting Cheese Pizza",
        subtitle = "Pizza Italiano",
        rating = "4.5",
        image = R.drawable.pizza,
        sizes = listOf(
            SizeOptionUI("s", "Small", "9.99 L.E.", false),
            SizeOptionUI("m", "Medium", "12.00 L.E.", true),
            SizeOptionUI("l", "Large", "16.89 L.E.", false)
        ),
        addOns = listOf(
            AddOnUI("a1", "mushroom", "50 gm +5.40 L.E.", R.drawable.mushroom, true),
            AddOnUI("a2", "Extra Cheese", "30 gm +10.40 L.E.", R.drawable.cheese, false)
        ),
        qty = 1,
        totalText = "407.4 L.E."
    )
    DescriptionScreen(
        state = s,
        onBack = {},
        onSelectSize = {},
        onToggleAddOn = {},
        onDecQty = {},
        onIncQty = {},
        onDelete = {},
        onAddToCart = {}
    )
}
