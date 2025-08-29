package com.example.milkchequedemo.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.milkchequedemo.domain.model.StoreInfo
import com.example.milkchequedemo.presentation.viewmodel.WelcomeViewModel
import com.example.milkchequedemo.ui.theme.MilkChequeDemoTheme
import com.example.milkchequedemo.utils.ResponseWrapper


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    storeId: Int,
    tableId: Int,
    onContinue: () -> Unit = {}
) {
    val vm: WelcomeViewModel = hiltViewModel()
    val s by vm.state.collectAsState()

    LaunchedEffect(storeId, tableId) { vm.load(storeId, tableId) }

    when (val st = s) {
        is ResponseWrapper.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        is ResponseWrapper.Error -> Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(st.message)
            Spacer(Modifier.height(16.dp))
            Button(onClick = { vm.load(storeId, tableId) }) { Text("Retry") }
        }
        is ResponseWrapper.Success<*> -> {
            val data = (st as ResponseWrapper.Success<StoreInfo>).data
            val name = data?.storeName.orEmpty()
            val loc = data?.storeLocation.orEmpty()
            val table = data?.tableId.orEmpty()
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("MilkCheque", fontSize = 28.sp, fontWeight = FontWeight.SemiBold) }
                    )
                }
            ) { inner ->
                Column(
                    Modifier.fillMaxSize().padding(inner).padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Welcome!", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(8.dp))
                    Text(name, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(4.dp))
                    Text(loc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(12.dp))
                    Text("Table #$table", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(24.dp))
                    Button(onClick = onContinue, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(18.dp)) {
                        Text("Let’s Go")
                    }
                }
            }
        }
        ResponseWrapper.Idle -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
    }
}





