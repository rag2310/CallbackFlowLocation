package com.rago.callbackflowlocation.ui.welcome

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rago.callbackflowlocation.R
import com.rago.callbackflowlocation.utils.CheckPermissions

@Composable
fun WelcomeScreen(
    welcomeViewModel: WelcomeViewModel,
    onNav: () -> Unit
) {



    val allPermission by welcomeViewModel.allPermissions.collectAsState()

    WelcomeContent(onPermission = {
        welcomeViewModel.checkPermission {
            onNav()
        }
    }, allPermission = allPermission)
}

@Preview(showBackground = true)
@Composable
fun WelcomeContent(
    onPermission: () -> Unit = {},
    allPermission: Boolean = true
) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        onPermission()
    }

    if (!allPermission) {
        AlertDialog(onDismissRequest = { }, buttons = {
            TextButton(onClick = {
                launcher.launch(CheckPermissions.REQUIRED_PERMISSIONS_APP)
            }) {
                Text(text = "Ok")
            }
        }, title = { Text(text = "Warning") }, text = { Text(text = "Permission") })
    }

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxSize()
                .weight(1f)
                .background(Color.White)
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomEnd = 30.dp))
                    .background(Color.Cyan),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.h4
                )
            }
        }
        Row(
            Modifier
                .fillMaxSize()
                .weight(2f)
                .background(Color.Cyan)
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp))
                    .background(Color.White)
            ) {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            onPermission()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp)
                    ) {
                        Text(text = "My Map Google")
                    }
                }
            }
        }
    }
}