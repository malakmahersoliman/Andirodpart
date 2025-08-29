package com.example.milkchequedemo.presentation.screens.noneed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milkchequedemo.presentation.components.ReusableButton
import com.example.milkchequedemo.presentation.components.ReusableTextField
import com.example.milkchequedemo.ui.theme.MexicanRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgetPasswordScreen(
    email: String,
    onEmailChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onBackClick: () -> Unit,
    onBackToSignUpClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            Text(
                text = "Forget Password",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(32.dp))

            ReusableTextField(
                value = email,
                onValueChange = onEmailChange,
                placeholder = "Enter Your Email",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Back to Sign Up",
                color = MexicanRed,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onBackToSignUpClick() }
            )

            Spacer(Modifier.height(24.dp))

            ReusableButton(
                text = "Send",
                onClick = onSendClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgetPasswordScreenPreview() {
    ForgetPasswordScreen(
        email = "",
        onEmailChange = {},
        onSendClick = {},
        onBackClick = {},
        onBackToSignUpClick = {}
    )
}

