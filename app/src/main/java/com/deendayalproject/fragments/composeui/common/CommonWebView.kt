package com.deendayalproject.fragments.composeui.common


import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.viewinterop.AndroidView
import androidx.activity.compose.BackHandler

@Composable
fun CommonWebView(
    url: String,
    onClose: () -> Unit
) {

    var webView: WebView? by remember { mutableStateOf(null) }

    BackHandler {
        if (webView?.canGoBack() == true) {
            webView?.goBack()
        } else {
            onClose()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {

        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    "Web View",
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { onClose() }) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    WebView(ctx).apply {

                        webViewClient = object : WebViewClient() {

                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                url: String?
                            ): Boolean {
                                url?.let { view?.loadUrl(it) }
                                return true
                            }
                        }

                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.loadsImagesAutomatically = true
                        settings.mixedContentMode =
                            android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

                        loadUrl(url)

                        webView = this
                    }
                }
            )
        }
    }
}