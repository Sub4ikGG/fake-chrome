package com.android.google.remote.retrofit

import com.android.google.remote.server.ServerConstants.BASE_URL
import com.android.google.remote.server.ServerConstants.DEVICE_ID_HEADER
import com.android.google.remote.server.ServerConstants.IP_HEADER
import com.android.google.remote.server.ServerConstants.MAC_HEADER
import com.android.google.remote.server.ServerService
import com.android.google.utils.User
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/*
* Retrofit2 using for network requests
* */

object Retrofit {

    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .addInterceptor { chain -> // Interceptor for auto-adding headers to service requests
            val request: Request = chain.request().newBuilder()
                .addHeader(IP_HEADER, User.getIp().orEmpty())
                .addHeader(MAC_HEADER, User.getMac().orEmpty())
                .addHeader(DEVICE_ID_HEADER, User.getDeviceId().orEmpty())
                .build()

            chain.proceed(request)
        }
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val serverService = retrofit.create(ServerService::class.java)

    fun getService(): ServerService = serverService

}