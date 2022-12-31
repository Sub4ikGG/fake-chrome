package com.android.google.services.screenshoot

import android.graphics.Bitmap

interface IScreenshotService {
    fun takeScreenshot(): Bitmap
}