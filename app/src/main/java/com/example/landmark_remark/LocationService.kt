package com.example.landmark_remark

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task

class LocationService(private val context: Application) {

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation() : Task<Location?> {
        val cancelToken = CancellationTokenSource()
        return LocationServices.getFusedLocationProviderClient(context).getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, cancelToken.token)
    }
}