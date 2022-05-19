package com.rago.callbackflowlocation.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import com.rago.callbackflowlocation.utils.hasPermission
import com.rago.callbackflowlocation.utils.toText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit

class SharedLocationManager constructor(
    private val context: Context,
    externalScope: CoroutineScope
) {

    private val _receivingLocationUpdates: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val receivingLocationUpdates: StateFlow<Boolean>
        get() = _receivingLocationUpdates

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest = LocationRequest.create().apply {
        interval = TimeUnit.SECONDS.toMillis(1)
        fastestInterval = TimeUnit.SECONDS.toMillis(1)
        maxWaitTime = TimeUnit.SECONDS.toMillis(1)
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    @SuppressLint("MissingPermission")
    private val _locationUpdates = callbackFlow {
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                Log.i(TAG, "New Location: ${result.lastLocation.toText()}")
                trySend(result.lastLocation)
            }
        }

        if (!context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
            !context.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            close()
        }

        _receivingLocationUpdates.value = true

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        ).addOnFailureListener { e ->
            close(e)
        }

        awaitClose {
            fusedLocationClient.removeLocationUpdates(callback)
        }
    }.shareIn(externalScope, replay = 0, started = SharingStarted.WhileSubscribed())

    fun locationFlow(): Flow<Location> = _locationUpdates

    companion object {
        private const val TAG = "SharedLocationManager"
    }
}