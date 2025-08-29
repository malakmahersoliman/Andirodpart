package com.example.milkchequedemo.presentation.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory

@Composable
fun EmbeddedQrScanner(
    modifier: Modifier = Modifier,
    onScanned: (String) -> Unit,
    torchOn: Boolean = false,
    acceptedFormats: List<BarcodeFormat> = listOf(BarcodeFormat.QR_CODE)
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    // Debounce repeats
    var lastText by remember { mutableStateOf<String?>(null) }

    // Keep a ref to the created DecoratedBarcodeView for lifecycle control
    var dbvRef: DecoratedBarcodeView? by remember { mutableStateOf(null) }

    val callback = remember {
        BarcodeCallback { result: BarcodeResult? ->
            val text = result?.text ?: return@BarcodeCallback
            if (text != lastText) {
                lastText = text
                onScanned(text)
            }
        }
    }

    // Pause/Resume with lifecycle
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> dbvRef?.resume()
                Lifecycle.Event.ON_PAUSE -> dbvRef?.pause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    if (!hasCameraPermission) {
        Box(modifier = modifier) { }
        return
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            DecoratedBarcodeView(ctx).apply {
                // IMPORTANT: set decoder on the *inner* barcodeView of DecoratedBarcodeView
                this.barcodeView.decoderFactory = DefaultDecoderFactory(acceptedFormats)
                if (torchOn) setTorchOn() else setTorchOff()
                decodeContinuous(callback)
                resume()
            }.also { created ->
                dbvRef = created
            }
        },
        update = { view ->
            if (torchOn) view.setTorchOn() else view.setTorchOff()
        }
    )
}