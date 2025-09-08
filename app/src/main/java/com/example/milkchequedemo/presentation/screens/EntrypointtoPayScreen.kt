package com.example.milkchequedemo.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.milkchequedemo.R
import com.example.milkchequedemo.presentation.components.ReusableButton
import com.example.milkchequedemo.ui.theme.MexicanRed

@Composable
fun EntrypointtoPayScreen(
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit = {},
    onProceedClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top card with image
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.happy),
                contentDescription = "Order celebration",
                modifier = Modifier
                    .widthIn(max = 248.dp)
                    .height(169.dp)
                    .fillMaxWidth()
            )
        }

        Spacer(Modifier.height(24.dp))

        // Title
        Text(
            text = "Order Placed Successfully!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MexicanRed,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(12.dp))

        // Subtitle
        Text(
            text = "Your order is being prepared in the kitchen",
            style = MaterialTheme.typography.bodyMedium,
            color = MexicanRed,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(32.dp))


        ReusableButton(
            text = "Edit to Order",
            onClick = onEditClick,
            modifier = Modifier.fillMaxWidth(),
            height = 52.dp
        )
        Spacer(Modifier.height(16.dp))

        ReusableButton(
            text = "Proceed to Payment",
            onClick = onProceedClick,
            modifier = Modifier.fillMaxWidth(),
            height = 52.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EntrypointtoPayScreenPreview() {
    MaterialTheme {
        EntrypointtoPayScreen()
    }
}
