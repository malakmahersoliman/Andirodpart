package com.example.milkchequedemo.presentation.screens.noneed

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milkchequedemo.presentation.components.ReusableButton
import com.example.milkchequedemo.presentation.components.ReusableTextField
import com.example.milkchequedemo.ui.theme.HintOfRed
import com.example.milkchequedemo.ui.theme.MexicanRed
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignupScreen(
    onSignUp: ( name: String, email: String, phone: String, password: String) -> Unit = { _,_,_,_ -> },
    onLoginClick: () -> Unit = {},
    onContinueAsGuest: () -> Unit = {},
    isSubmitting: Boolean = false
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    val focus = LocalFocusManager.current

    // --- Validation ---
    val nameOk = { s: String -> s.trim().length >= 2 }
    val emailOk = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(email)
    val phoneOk = Regex("^01([0125])\\d{8}$").matches(phone) // Egypt mobile
    var dob by rememberSaveable { mutableStateOf("") }          // will be like "2004-12-27"
    var showDobPicker by rememberSaveable { mutableStateOf(false) }
    val passOk = pass.length >= 6
    val confirmOk = confirm == pass

    val nameErr = if (name.isNotEmpty() && !nameOk(name)) "Enter at least 2 characters." else null
    val emailErr = if (email.isNotEmpty() && !emailOk) "Enter a valid email." else null
    val phoneErr = if (phone.isNotEmpty() && !phoneOk) "Enter a valid Egyptian phone (010/011/012/015 + 8 digits)." else null
    val passErr  = if (pass.isNotEmpty()  && !passOk)  "At least 6 characters." else null
    val confErr  = if (confirm.isNotEmpty() && !confirmOk) "Passwords do not match." else null

    val canSubmit =
        name.isNotBlank()  && email.isNotBlank() && phone.isNotBlank() &&
                pass.isNotBlank() && confirm.isNotBlank() &&
                nameErr == null  && emailErr == null &&
                phoneErr == null && passErr == null && confErr == null
    val datePickerState = rememberDatePickerState()
    if (showDobPicker) {
        DatePickerDialog(
            onDismissRequest = { showDobPicker = false },
            confirmButton = {
                Text(
                    text = "OK",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            val millis = datePickerState.selectedDateMillis
                            if (millis != null) {
                                dob = millisToIso(millis)
                            }
                            showDobPicker = false
                        }
                )
            },
            dismissButton = {
                Text(
                    text = "Cancel",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { showDobPicker = false }
                )
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HintOfRed)
            .systemBarsPadding()
            .imePadding()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))

        Text(text = "Sign Up", fontWeight = FontWeight.Bold, fontSize = 24.sp)

        Spacer(Modifier.height(24.dp))

            ReusableTextField(
                value = name,
                onValueChange = { name = it },
                label = "Full Name",
                imeAction = ImeAction.Next,
                error = nameErr,
                modifier = Modifier.fillMaxWidth()
            )

        Spacer(Modifier.height(8.dp))

        ReusableTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            placeholder = "name@example.com",
            imeAction = ImeAction.Next,
            error = emailErr
        )

        Spacer(Modifier.height(12.dp))

        ReusableTextField(
            value = phone,
            onValueChange = { phone = it },
            label = "Phone number",
            placeholder = "010xxxxxxxx",
            imeAction = ImeAction.Next,
            error = phoneErr
        )
        Spacer(Modifier.height(12.dp))
        ReusableTextField(
            value = dob,
            onValueChange = { /* readOnly, ignore manual typing */ },
            label = "Enter Your Date Of Birth",
            placeholder = "YYYY-MM-DD",
            readOnly = true,
            onClick = { showDobPicker = true },
            trailing = {
                IconButton(onClick = { showDobPicker = true }) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = "Pick date"
                    )
                }
            }
        )
        Spacer(Modifier.height(12.dp))

        ReusableTextField(
            value = pass,
            onValueChange = { pass = it },
            label = "Password",
            isPassword = true,
            imeAction = ImeAction.Next,
            error = passErr
        )

        Spacer(Modifier.height(12.dp))

        ReusableTextField(
            value = confirm,
            onValueChange = { confirm = it },
            label = "Confirm password",
            isPassword = true,
            imeAction = ImeAction.Done,
            onImeAction = {
                focus.clearFocus()
                if (canSubmit) onSignUp(name, email, phone, pass)
            },
            error = confErr
        )

        Spacer(Modifier.height(24.dp))

        ReusableButton(
            text = "Sign up",
            onClick = {
                focus.clearFocus()
                onSignUp(name, email, phone, pass)
            },
            enabled = canSubmit,
            isLoading = isSubmitting
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Already have an account?", modifier = Modifier.alignByBaseline())
            Spacer(Modifier.width(6.dp))
            Text(
                text = "Login",
                color = MexicanRed,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .alignByBaseline()
                    .clickable { onLoginClick() }
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Continue as Guest",
            color = MexicanRed,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable { onContinueAsGuest() }
        )

        Spacer(Modifier.height(24.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun millisToIso(millis: Long): String {
    val date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(DateTimeFormatter.ISO_LOCAL_DATE) // e.g., 2004-12-27
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignupPreview() {
    SignupScreen()
}
