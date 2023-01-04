package com.android.google.utils

object User {
    private val info = UserInfo()

    fun fill(
        ip: String? = info.ip,
        mac: String? = info.mac,
        deviceId: String? = info.deviceId
    ) {
        this.info.ip = ip
        this.info.mac = mac
        this.info.deviceId = deviceId
    }

    fun getIp() = info.ip
    fun getMac() = info.mac
    fun getDeviceId() = info.deviceId

}
