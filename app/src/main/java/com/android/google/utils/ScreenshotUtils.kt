package com.android.google.utils

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun Bitmap.toBase64(): String {
    val yteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, yteArrayOutputStream)
    val b = yteArrayOutputStream.toByteArray()
    return Base64.getEncoder().encodeToString(b)
}