package com.example.milkchequedemo.presentation.screens.noneed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.milkchequedemo.presentation.components.ReusableButton
import com.example.milkchequedemo.presentation.components.ReusableTextField
import com.example.milkchequedemo.ui.theme.HintOfRed
import com.example.milkchequedemo.ui.theme.MexicanRed

@Composable
fun LoginScreen(
    onLogin: (phone: String, password: String) -> Unit = { _, _ -> },
    onForgotPassword: () -> Unit = {},
    onSignUp: () -> Unit = {},
    isSubmitting: Boolean = false
) {
    //todo this will be add into viewmodel and passed as param (state hoisting)
    var phone by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // Egypt mobile: starts with 010/011/012/015 + 8 digits
    val phoneValid = Regex("^01(0|1|2|5)\\d{8}$").matches(phone)
    val phoneError: String? = when {
        phone.isEmpty() -> null
        !phoneValid -> "Enter a valid Egyptian phone number (e.g., 010xxxxxxxx)."
        else -> null
    }
    val passError: String? = when {
        password.isEmpty() -> null
        password.length < 6 -> "Password must be at least 6 characters."
        else -> null
    }

    val canSubmit = phone.isNotBlank() && password.isNotBlank() && phoneError == null && passError == null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HintOfRed)
            .systemBarsPadding()
            .imePadding()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO: App logo/image here

        Text(
            text = "Login",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Spacer(Modifier.height(32.dp))

        ReusableTextField(
            value = phone,
            onValueChange = { phone = it },
            label = "Phone number",
            placeholder = "010xxxxxxxx",
            imeAction = ImeAction.Next,
            error = phoneError
        )

        Spacer(Modifier.height(12.dp))

        ReusableTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            isPassword = true,
            imeAction = ImeAction.Done,
            onImeAction = {
                focusManager.clearFocus()
                if (canSubmit) onLogin(phone, password)
            },
            error = passError
        )

        Spacer(Modifier.height(1.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Forgot Password?",
                color = MexicanRed,
                modifier = Modifier.clickable { onForgotPassword() }
            )
        }

        Spacer(Modifier.height(8.dp))

        ReusableButton(
            text = "Login",
            onClick = {
                focusManager.clearFocus()
                onLogin(phone, password)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50),
            height = 48.dp,
            enabled = canSubmit,
            isLoading = isSubmitting
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Don’t have an account?", modifier = Modifier.alignByBaseline())
            Spacer(Modifier.width(6.dp))
            Text(
                text = "Sign Up",
                color = MexicanRed,
                modifier = Modifier
                    .alignByBaseline()
                    .clickable { onSignUp() }
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen()
}
