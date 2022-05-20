package com.rago.callbackflowlocation.ui.map

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun MapScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    mapViewModel: MapViewModel
) {

    var locationFlow: Job?

    var location by remember {
        mutableStateOf<LatLng?>(null)
    }

    val cameraPositionState = rememberCameraPositionState {}

    var cameraMoving by remember {
        mutableStateOf(false)
    }

    var setPosition by remember {
        mutableStateOf(false)
    }

    DisposableEffect(key1 = lifecycleOwner, effect = {
        locationFlow = mapViewModel.location.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        ).onEach {
            location = LatLng(it.latitude, it.longitude)
            if (!cameraMoving) {
                setPosition = true
                cameraPositionState.position =
                    CameraPosition.fromLatLngZoom(LatLng(it.latitude, it.longitude), 10f)
            }
        }.launchIn(lifecycleOwner.lifecycleScope)
        onDispose {
            locationFlow?.cancel()
        }
    })

    if (cameraPositionState.isMoving) {
        if (!cameraMoving && !setPosition){
            cameraMoving = true
        }
    } else {
        setPosition = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            location?.let {
                Marker(
                    position = it,
                    title = "Singapore",
                    snippet = "Marker in Singapore"
                )
            }
        }
        Column(Modifier.fillMaxSize()) {
            AnimatedVisibility(visible = cameraMoving) {
                Button(onClick = {
                    cameraMoving = false
                }) {
                    Text(text = "Centrar")
                }
            }
        }
    }
}