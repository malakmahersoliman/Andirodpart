package com.example.milkchequedemo.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.milkchequedemo.data.config.AppConfig

@Composable
fun ErrorHandler(
    errorMessage: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(AppConfig.UI.PADDING_LARGE.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = AppConfig.UI.CARD_ELEVATION.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
        ) {
            Column(
                modifier = Modifier.padding(AppConfig.UI.PADDING_LARGE.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "Error",
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.padding(bottom = AppConfig.UI.PADDING_MEDIUM.dp)
                )
                
                Text(
                    text = "Oops! Something went wrong",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD32F2F)
                )
                
                Spacer(modifier = Modifier.padding(AppConfig.UI.PADDING_SMALL.dp))
                
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF424242)
                )
                
                onRetry?.let { retryAction ->
                    Spacer(modifier = Modifier.padding(AppConfig.UI.PADDING_MEDIUM.dp))
                    ReusableButton(
                        text = "Try Again",
                        onClick = retryAction
                    )
                }
            }
        }
    }
}
