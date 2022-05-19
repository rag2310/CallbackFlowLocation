package com.rago.callbackflowlocation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rago.callbackflowlocation.service.ForegroundOnlyLocationService
import com.rago.callbackflowlocation.ui.map.MapScreen
import com.rago.callbackflowlocation.ui.map.MapViewModel
import com.rago.callbackflowlocation.ui.theme.CallbackFlowLocationTheme
import com.rago.callbackflowlocation.ui.welcome.WelcomeScreen
import com.rago.callbackflowlocation.ui.welcome.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var foregroundOnlyLocationServiceBound = false
    private var foregroundOnlyLocationService: ForegroundOnlyLocationService? = null

    private val foregroundOnlyLocationServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundOnlyLocationService.LocalBinder
            foregroundOnlyLocationService = binder.service
            foregroundOnlyLocationServiceBound = true
           /* foregroundOnlyLocationService?.subscribeToLocationUpdates() ?: Log.i(
                "LocationService",
                "Service Not Bound"
            )*/
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            foregroundOnlyLocationService?.unSubscribeToLocationUpdates()
            foregroundOnlyLocationService = null
            foregroundOnlyLocationServiceBound = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CallbackFlowLocationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "welcome") {
                        composable("welcome") {
                            val welcomeViewModel: WelcomeViewModel = hiltViewModel()
                            WelcomeScreen(
                                welcomeViewModel = welcomeViewModel
                            ) {
                                initService()
                                navController.navigate("map")
                            }
                        }
                        composable("map") {
                            val mapViewModel: MapViewModel = hiltViewModel()
                            MapScreen(
                                lifecycleOwner = this@MainActivity,
                                mapViewModel = mapViewModel
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, ForegroundOnlyLocationService::class.java).also { intent ->
            this.bindService(
                intent,
                foregroundOnlyLocationServiceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    override fun onStop() {
        if (foregroundOnlyLocationServiceBound) {
            this.unbindService(foregroundOnlyLocationServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
        super.onStop()
    }

    private fun initService() {
        foregroundOnlyLocationService?.subscribeToLocationUpdates() ?: Log.i(
            "LocationService",
            "Service Not Bound"
        )
    }
}