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
import com.example.milkchequedemo.R
import com.example.milkchequedemo.ui.theme.White

// -------------------- Reusable pieces --------------------

@Composable
private fun FoodCard(
    @DrawableRes imageRes: Int,
    title: String,
    price: String,
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
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(10.dp))

            Text(text = title, fontWeight = FontWeight.SemiBold)
            Text(text = price, fontSize = 13.sp, color = Color.Gray)

            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onAddClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_box), // add a small plus icon in drawable
                        contentDescription = "Add"
                    )
                }
            }
        }
    }
}

// -------- UI models (presentation layer) --------
data class MenuUiState(
    val greetingName: String = "MilkCheque Agency",
    val categories: List<CategoryUI> = emptyList(),
    val bestSellers: List<ProductUI> = emptyList(),
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
    onProductClick: (productId: String) -> Unit,
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
                        icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart") }
                    )
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
                    NavigationBarItem(
                        selected = state.selectedTab == BottomTab.Settings,
                        onClick = { onTabSelected(BottomTab.Settings) },
                        icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") }
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
                                    imageRes = p.imageRes,
                                    title = p.title,
                                    price = p.priceText,
                                    onAddClick = { onAddItem(p.id) },
                                    onOpenDetails = { onProductClick(p.id) }
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
            ProductUI("p1", R.drawable.pizza, "Melting Cheese Pizza", "390.00 L.E"),
            ProductUI("p2", R.drawable.pasta, "Penne all'Arrabbiata", "258.00 L.E"),
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
