package com.example.milkchequedemo.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milkchequedemo.ui.theme.MexicanRed
import com.example.milkchequedemo.ui.theme.White

@Composable
fun ReusableButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    height: Dp = 48.dp,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(20.dp),
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        shape = shape,
        colors = ButtonDefaults.buttonColors(containerColor = MexicanRed)
    ) {
        if (isLoading) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = White
                )
                Text(text = "Please wait…", color = White, fontSize = 16.sp)
            }
        } else {
            Text(text = text.uppercase(), color = White, fontSize = 16.sp)
        }
    }
}

@Preview
@Composable
private fun bbb() {

}