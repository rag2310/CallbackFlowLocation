package com.rago.callbackflowlocation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope

@HiltAndroidApp
class CallbackFlowLocationApplication : Application() {
    val applicationScope = GlobalScope
}