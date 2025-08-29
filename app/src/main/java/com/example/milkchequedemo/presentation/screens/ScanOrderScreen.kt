@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.milkchequedemo.presentation.screens





import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milkchequedemo.R
import com.example.milkchequedemo.presentation.components.ReusableButton
import com.example.milkchequedemo.presentation.navigation.Routes
import kotlinx.coroutines.launch

@Composable
fun ScanOrderScreen(
    modifier: Modifier = Modifier,
    // passed from AppNavGraph: { route -> navController.navigate(route) }
    navigateTo: (String) -> Unit
) {
    var scanning by remember { mutableStateOf(false) }
    var torchOn by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("MilkCheque", fontSize = 28.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                },
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.milk_tea),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { inner ->
        Box(Modifier.fillMaxSize().padding(inner)) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(32.dp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(0.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (scanning) {
                            EmbeddedQrScanner(
                                modifier = Modifier.fillMaxSize(),
                                torchOn = torchOn,
                                onScanned = { code ->
                                    scanning = false

                                    // Parse "1|123"
                                    val parts = code.trim().split("|")
                                    val storeId = parts.getOrNull(0)?.trim()?.toIntOrNull()
                                    val tableId = parts.getOrNull(1)?.trim()?.toIntOrNull()
                                    if (storeId == null || tableId == null) {
                                        scope.launch { snackbarHostState.showSnackbar("Invalid QR format") }
                                        return@EmbeddedQrScanner
                                    }

                                    // TODO: navigate or send to ViewModel
                                     navigateTo(Routes.welcome(storeId, tableId))
                                }
                            )
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Tap 'Scan Me' to start scanning ✨",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                ReusableButton(
                    text = if (scanning) "Stop Scan" else "Scan Me",
                    onClick = { scanning = !scanning },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
            }

            if (loading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
