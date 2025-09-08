package com.example.milkchequedemo.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milkchequedemo.presentation.components.ReusableButton
import com.example.milkchequedemo.ui.theme.MexicanRed
import com.example.milkchequedemo.ui.theme.White
import com.example.milkchequedemo.ui.theme.MilkChequeDemoTheme
import kotlinx.coroutines.delay

/**
 * Stateless UI that exactly follows your design.
 * Pass in callbacks from your NavGraph/ViewModel.
 */
@Composable
fun AfterPaymentScreen(
    modifier: Modifier = Modifier,
    title: String = "Thank You!",
    subtitle: String = "Payment done Successfully \uD83D\uDCB8",
    note: String = "You will be redirected to the home page shortly or click here to return to home page",
    onHomeClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))

        // Big red circle with white check
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(MexicanRed),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Success",
                tint = White,
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(Modifier.height(32.dp))

        Text(
            text = title,
            color = MexicanRed,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = subtitle,
            color = MexicanRed,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = note,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        ReusableButton(
            text = "Home",
            onClick = onHomeClick,
            height = 56.dp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))
    }
}

/**
 * Optional wrapper that auto-navigates home after [autoNavigateMillis].
 * Use this from your NavGraph if you want the auto-redirect behavior.
 */
@Composable
fun AfterPaymentRoute(
    onGoHome: () -> Unit,
    autoNavigateMillis: Long = 2500,
    enableAutoNavigate: Boolean = true
) {
    if (enableAutoNavigate) {
        LaunchedEffect(Unit) {
            delay(autoNavigateMillis)
            onGoHome()
        }
    }
    AfterPaymentScreen(onHomeClick = onGoHome)
}

@Preview(showBackground = true)
@Composable
private fun AfterPaymentPreview() {
    MilkChequeDemoTheme {
        AfterPaymentScreen(onHomeClick = {})
    }
}
