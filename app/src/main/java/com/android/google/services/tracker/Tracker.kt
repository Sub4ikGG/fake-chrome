package com.android.google.services.tracker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.android.google.BuildConfig
import com.android.google.utils.logw
import com.google.android.gms.location.*
import com.android.google.R

class Tracker : Service() {

    private var getLocationMessage: String = ""

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate()")
        prepare()
        getLocationMessage = ""
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand()")
        subscribe()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        unsubscribe() // Отписка от callback`a
        super.onDestroy()
    }

    private fun prepare() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val interval = 2000L
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, interval)
            .setMinUpdateIntervalMillis(interval)
            .setMaxUpdateDelayMillis(interval)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                locationResult.lastLocation.let { location ->
                    currentLocation = location

                    Log.d(TAG, location.toString())
                    if (location != null) {
                        LocationService.setLocation(location = location)
                        if (BuildConfig.DEBUG) {
                            logw("Tracker. Current location: $location")
                        }
                    }

                }

            }

        }
    }

    private fun subscribe() {
        try {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager =
                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                val importance = NotificationManager.IMPORTANCE_LOW
                val channelId = "Chrome"
                val name = "Chrome"
                val mChannel = NotificationChannel(channelId, name, importance)
                notificationManager.createNotificationChannel(mChannel)

                val notification: Notification = Notification.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(getString(R.string.notification_title))
                    .setContentText(getString(R.string.notification_body))
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setChannelId(channelId)
                    .build()

                startForeground(1, notification)
            }
        } catch (ex: SecurityException) {
            Log.e(TAG, NO_PERMISSIONS_MESSAGE + ex)
        }
    }

    private fun unsubscribe() {
        val result = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        result.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, UNSUBSCRIBE_MESSAGE)
            } else {
                Log.d(TAG, UNSUBSCRIBE_ERROR_MESSAGE)
            }
        }
    }

    companion object {
        const val TAG = "TrackingService"

        private const val NO_PERMISSIONS_MESSAGE = "No location permissions... "
        private const val UNSUBSCRIBE_MESSAGE = "Location callback removed..."
        private const val UNSUBSCRIBE_ERROR_MESSAGE = "Can't remove location callback..."
    }
}