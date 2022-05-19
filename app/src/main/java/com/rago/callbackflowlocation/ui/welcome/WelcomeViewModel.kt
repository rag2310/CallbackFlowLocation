package com.rago.callbackflowlocation.ui.welcome

import androidx.lifecycle.ViewModel
import com.rago.callbackflowlocation.utils.CheckPermissions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val checkPermissions: CheckPermissions,
) : ViewModel() {


    private val _allPermissions = MutableStateFlow(true)
    val allPermissions: StateFlow<Boolean> = _allPermissions

    fun checkPermission(onNav: () -> Unit) {
        when {
            !checkPermissions.allPermissionsGranted() -> {
                _allPermissions.value = false
            }
            else -> {
                _allPermissions.value = true
                onNav()
            }
        }
    }
}