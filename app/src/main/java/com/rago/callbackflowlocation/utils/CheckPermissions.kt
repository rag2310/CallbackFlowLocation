package com.rago.callbackflowlocation.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import javax.inject.Inject

class CheckPermissions @Inject constructor(private val context: Context) {
    companion object {

        val REQUIRED_PERMISSIONS_APP =
            mutableListOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ).apply {
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }*/
                add(Manifest.permission.ACCESS_COARSE_LOCATION)
            }.toTypedArray()
    }

    fun allPermissionsGranted() = REQUIRED_PERMISSIONS_APP.all {
        ContextCompat.checkSelfPermission(
            context, it
        ) == PackageManager.PERMISSION_GRANTED
    }
}