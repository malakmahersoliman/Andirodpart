package com.example.milkchequedemo.presentation.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

// -------------------- Reusable pieces --------------------

@Composable
private fun FoodCard(
    @DrawableRes imageRes: Int,
    title: String,
    price: String,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp),
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

@Composable
private fun CategoryRow(categories: List<Pair<Int, String>>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { (icon, label) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = label,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(6.dp))
                Text(text = label, fontSize = 13.sp)
            }
        }
    }
}

// -------------------- Screen --------------------

@Composable
fun DigitalMenuScreen(
    onBellClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onSeeAllClick: () -> Unit = {},
    onAddItem: (String) -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart") },
                    label = { Text("Cart") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") }
                )
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
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
                    Text("MilkCheck Agency", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBellClick) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                }
            }

            // Categories
            CategoryRow(
                categories = listOf(
                    R.drawable.coka to "Drinks",
                    R.drawable.pizza to "Pizza",
                    R.drawable.pasta to "Pasta",
                    R.drawable.dessert to "Dessert"
                )
            )

            // Special Offers
            Text(
                "Special Offers",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Image(
                painter = painterResource(R.drawable.offer_banner),
                contentDescription = "Offer",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.Crop
            )

            // Best Sellers Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Best Sellers", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("See All", color = MaterialTheme.colorScheme.primary, modifier = Modifier)
            }

            // Best Sellers list
            LazyRow(
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FoodCard(
                        imageRes = R.drawable.pizza,
                        title = "Melting Cheese Pizza",
                        price = "390.00 L.E",
                        onAddClick = { onAddItem("Melting Cheese Pizza") }
                    )
                }
                item {
                    FoodCard(
                        imageRes = R.drawable.pasta,
                        title = "Penne all'Arrabbiata",
                        price = "258.00 L.E",
                        onAddClick = { onAddItem("Penne all'Arrabbiata") }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

// -------------------- Preview --------------------

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DigitalMenuPreview() {
    DigitalMenuScreen()
}
