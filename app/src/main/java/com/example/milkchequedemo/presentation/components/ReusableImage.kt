package com.example.milkchequedemo.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ReusableImage(
    @DrawableRes resId: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    cornerRadius: Dp = 0.dp,
    onClick: (() -> Unit)? = null
) {
    val imageModifier = modifier
        .size(size)

    if (onClick != null) {
        androidx.compose.foundation.Image(
            painter = painterResource(id = resId),
            contentDescription = contentDescription,
            modifier = imageModifier.clickable { onClick() }
        )
    } else {
        androidx.compose.foundation.Image(
            painter = painterResource(id = resId),
            contentDescription = contentDescription,
            modifier = imageModifier
        )
    }
}
