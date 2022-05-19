package com.rago.callbackflowlocation.ui.map

import androidx.lifecycle.ViewModel
import com.rago.callbackflowlocation.data.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    repository: LocationRepository
) : ViewModel() {
    val location = repository.getLocations()
}