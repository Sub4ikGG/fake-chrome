package com.android.google.services.tracker

import android.location.Location
import com.android.google.services.tracker.models.TrackerLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

object LocationService {
    private val location: MutableStateFlow<TrackerLocation> = MutableStateFlow(TrackerLocation())

    fun getLocationFlow(): Flow<TrackerLocation> = flow {
        location.collect {
            emit(it)
        }
    }

    fun getLocation(): TrackerLocation = location.value

    fun setLocation(location: Location) {
        this.location.value =
            TrackerLocation(location.latitude, location.longitude)
    }
}