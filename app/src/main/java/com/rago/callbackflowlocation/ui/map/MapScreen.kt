package com.rago.callbackflowlocation.ui.map

import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.rago.callbackflowlocation.utils.toText
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun MapScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    mapViewModel: MapViewModel
) {

    var location by remember {
        mutableStateOf<Location?>(null)
    }

    var textLocation by remember {
        mutableStateOf("")
    }

    var locationFlow: Job?

    DisposableEffect(key1 = lifecycleOwner, effect = {
        locationFlow = mapViewModel.location.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        ).onEach {
            location = it
            textLocation = "$textLocation\n${it.toText()}"
        }.launchIn(lifecycleOwner.lifecycleScope)
        onDispose {
            locationFlow?.cancel()
        }
    })

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = textLocation)
    }
}