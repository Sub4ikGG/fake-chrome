package com.android.google.services.screenshoot

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View

/*
* Service for taking screenshots
* */

class ScreenshotService(private val view: View): IScreenshotService {

    override fun takeScreenshot(): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background

        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }

        view.draw(canvas)
        return bitmap
    }

}