package com.deendayalproject.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.deendayalproject.databinding.ActivityWebviewBinding
import com.google.android.material.appbar.MaterialToolbar

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val url = intent.getStringExtra("url") ?: ""

        binding.webView.settings.apply {

            // 🔹 Core
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true

            // 🔹 Zoom (optional but good UX)
            builtInZoomControls = true
            displayZoomControls = false

            // 🔹 Viewport (important for responsive websites)
            loadWithOverviewMode = true
            useWideViewPort = true

            // 🔹 Performance
            setRenderPriority(android.webkit.WebSettings.RenderPriority.HIGH)

            // 🔹 Cache (modern + fallback)
            cacheMode = android.webkit.WebSettings.LOAD_DEFAULT

            // 🔹 File access (safe)
            allowFileAccess = true
            allowContentAccess = true

            mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        }

//        binding.webView.webViewClient = object : WebViewClient() {
//
//            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//                binding.progressBar.visibility = View.VISIBLE
//            }
//
//            override fun onPageFinished(view: WebView?, url: String?) {
//                binding.progressBar.visibility = View.GONE
//
//                val js = """
//javascript:(function() {
//
//    function removeRealFooter() {
//document.body.lastElementChild?.remove();
//        // 🔴 Remove Angular wrapper (main)
//        document.querySelectorAll('app-outside-footer').forEach(e => e.remove());
//
//        // 🔴 Remove block-ui (parent container)
//        document.querySelectorAll('block-ui').forEach(e => e.remove());
//
//        // 🔴 Remove NIC footer by TEXT (100% working)
//        document.querySelectorAll("div").forEach(e => {
//            if (e.innerText &&
//                (e.innerText.includes("National Informatics Centre") ||
//                 e.innerText.includes("Copyright ©") ||
//                 e.innerText.includes("Terms & Conditions") ||
//                 e.innerText.includes("Privacy Policy"))) {
//
//                e.remove();
//            }
//        });
//
//        // 🔴 Remove top header strip (FAQs / Demo)
//        document.querySelectorAll("div").forEach(e => {
//            if (e.innerText &&
//                (e.innerText.includes("FAQs") &&
//                 e.innerText.includes("RTI") &&
//                 e.innerText.includes("Screen Reader Access"))) {
//
//                e.remove();
//            }
//        });
//
//    }
//
//    // 🔥 Run continuously (Angular fix)
//    setInterval(removeRealFooter, 200);
//
//})();
//""".trimIndent()
//                view?.evaluateJavascript(js, null)
//            }
//        }



        binding.webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: android.webkit.WebView?, url: String?, favicon: Bitmap?) {
                binding.progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                binding.progressBar.visibility = View.GONE
            }
        }

        binding.webView.loadUrl(url)
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}