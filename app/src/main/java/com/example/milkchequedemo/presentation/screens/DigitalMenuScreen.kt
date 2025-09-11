package com.example.milkchequedemo.presentation.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.milkchequedemo.R
import com.example.milkchequedemo.domain.model.MenuItem
import com.example.milkchequedemo.ui.theme.White

// -------------------- Reusable pieces --------------------

@Composable
private fun FoodCard(
    item: MenuItem,
    onAddClick: () -> Unit,
    onOpenDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp)
            .clickable { onOpenDetails() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {

            AsyncImage(
                model = item.iconUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(10.dp))

            Text(text = item.name, fontWeight = FontWeight.SemiBold)
            Text(text = item.price.toString(), fontSize = 13.sp, color = Color.Gray)

        }
    }
}

// -------- UI models (presentation layer) --------
data class MenuUiState(
    val greetingName: String = "MilkCheque Agency",
    val categories: List<CategoryUI> = emptyList(),
    val bestSellers: List<MenuItem> = emptyList(),
    val selectedTab: BottomTab = BottomTab.Home,
    val cartCount: Int = 0,                 // show sticky bar when > 0
)

data class CategoryUI(@DrawableRes val icon: Int, val label: String)
data class ProductUI(
    val id: String,
    @DrawableRes val imageRes: Int,
    val title: String,
    val priceText: String
)

enum class BottomTab { Home, Cart, Offers, Settings }

// -------- Screen --------
@Composable
fun DigitalMenuScreen(
    state: MenuUiState,
    onBellClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSeeAllClick: () -> Unit,
    onAddItem: (productId: String) -> Unit,
    onProductClick: (item:MenuItem) -> Unit,
    onCategoryClick: (label: String) -> Unit,
    onTabSelected: (BottomTab) -> Unit,
    onViewCartClick: () -> Unit
) {
    Box {
        Scaffold(
            bottomBar = {
                NavigationBar(containerColor = White) {
                    NavigationBarItem(
                        selected = state.selectedTab == BottomTab.Home,
                        onClick = { onTabSelected(BottomTab.Home) },
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Home") }
                    )
                    NavigationBarItem(
                        selected = state.selectedTab == BottomTab.Cart,
                        onClick = { onTabSelected(BottomTab.Cart) },
                        icon = { Icon(Icons.Filled.ShoppingCart,
                            contentDescription = "Cart",
                            modifier = Modifier.clickable {
                                onViewCartClick()
                            }) }
                    )
                    //todo navigation to static screen
                    NavigationBarItem(
                        selected = state.selectedTab == BottomTab.Offers,
                        onClick = { onTabSelected(BottomTab.Offers) },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.offer),
                                contentDescription = "Offers",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    )
                }
            }
        ) { inner ->

            // Prefer LazyColumn for perf
            LazyColumn(
                modifier = Modifier
                    .padding(inner)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    // Greeting + actions
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Hello 👋", fontSize = 16.sp)
                            Text(state.greetingName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onBellClick) {
                                Icon(
                                    Icons.Filled.Notifications,
                                    contentDescription = "Notifications"
                                )
                            }
                            IconButton(onClick = onSearchClick) {
                                Icon(Icons.Filled.Search, contentDescription = "Search")
                            }
                        }
                    }
                }

                // Categories
                if (state.categories.isNotEmpty()) {
                    item {
                        CategoryRow(
                            categories = state.categories,
                            onCategoryClick = onCategoryClick
                        )
                    }
                }

                // Special Offers banner
                item {
                    Text(
                        "Special Offers",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                item {
                    Image(
                        painter = painterResource(R.drawable.offer_banner),
                        contentDescription = "Special Offer Banner",
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(18.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                // Best Sellers header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Best Sellers", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(
                            "See All",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(4.dp)
                                .clickable { onSeeAllClick() }
                        )
                    }
                }

                // Best Sellers list
                if (state.bestSellers.isNotEmpty()) {
                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.bestSellers, key = { it.id }) { p ->
                                FoodCard(
                                    item=p,
                                    onAddClick = { onAddItem(p.id.toString()) },
                                    onOpenDetails = { onProductClick(p)}
                                )
                            }
                        }
                    }
                }

                // Spacer at bottom to avoid bottom bar overlap
                item { Spacer(Modifier.height(8.dp)) }
            }

            // Sticky “View Cart” bar (only when cart has items)
            if (state.cartCount > 0) {
                ViewCartBar(
                    count = state.cartCount,
                    totalText = "",           // fill from VM if you track total
                    onClick = onViewCartClick,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 72.dp) // above nav bar
                )
            }
        }
    }
}



// --- CategoryRow that uses UI models + click ---
//todo fake categories data
@Composable
private fun CategoryRow(
    categories: List<CategoryUI>,
    onCategoryClick: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { c ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(80.dp)
                    .clickable { onCategoryClick(c.label) }
            ) {
                Image(
                    painter = painterResource(id = c.icon),
                    contentDescription = c.label,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(6.dp))
                Text(text = c.label, fontSize = 13.sp)
            }
        }
    }
}

// Simple “View Cart” bar (optional)
@Composable
private fun ViewCartBar(
    count: Int,
    totalText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        tonalElevation = 4.dp,
        shadowElevation = 6.dp,
        shape = RoundedCornerShape(18.dp),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Row(
            Modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("View Your Cart ($count)")
            Text(totalText.ifEmpty { "" }, fontWeight = FontWeight.SemiBold)
        }
    }
}

// -------------------- Preview --------------------
@Preview(showBackground = true)
@Composable
private fun DigitalMenuPreview() {
    val state = MenuUiState(
        greetingName = "MilkCheque Agency",
        categories = listOf(
            CategoryUI(R.drawable.coka, "Drinks"),
            CategoryUI(R.drawable.pizza, "Pizza"),
            CategoryUI(R.drawable.pasta, "Pasta"),
            CategoryUI(R.drawable.dessert, "Dessert"),
        ),
        bestSellers = listOf(
            MenuItem(
                id = 5484,
                name = "Pizza",
                price = 4.5,
                description = "Melting Cheese Pizza",
                iconUrl = "data:image/jpeg;base64,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"
            )
        ),
        selectedTab = BottomTab.Home,
        cartCount = 2
    )
    DigitalMenuScreen(
        state = state,
        onBellClick = {},
        onSearchClick = {},
        onSeeAllClick = {},
        onAddItem = {},
        onProductClick = {},
        onCategoryClick = {},
        onTabSelected = {},
        onViewCartClick = {}
    )
}
