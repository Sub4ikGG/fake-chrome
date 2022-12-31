package com.android.google.viewmodel

import androidx.lifecycle.ViewModel
import com.android.google.constants.UrlConstants

class MainViewModel: ViewModel() {

    private var currentUrl: String = UrlConstants.baseURL

    fun setUrl(url: String) {
        currentUrl = url
    }

    fun getUrl() = currentUrl

}
