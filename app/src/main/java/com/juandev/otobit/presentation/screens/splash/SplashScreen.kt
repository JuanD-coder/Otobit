package com.juandev.otobit.presentation.screens.splash

import android.Manifest
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.juandev.otobit.R
import com.juandev.otobit.domain.model.PermissionsPreferenceState

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onContinue: () -> Unit,
) {

    val permissionState by viewModel.permissionsState.collectAsState()

    LaunchedEffect(permissionState) {
        if (permissionState.mediaPermission && permissionState.notificationsPermission) {
            onContinue()
        }
    }

    // --- LANZADORES DE PERMISOS ---
    val requestMediaPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.updatePermissionState(
                permissionState.copy(mediaPermission = isGranted).let {
                    // Opcional: También puedes actualizar legacyRead si es relevante para versiones antiguas
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                        it.copy(legacyRead = isGranted)
                    } else {
                        it
                    }
                }
            )
        }
    )

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                viewModel.updatePermissionState(permissionState.copy(notificationsPermission = isGranted))
            }
        }
    )

    if (permissionState.mediaPermission && permissionState.notificationsPermission) {
        LoadingIndicator()
    } else {
        SplashViewComponent(
            permissionState = permissionState,
            onContinue = onContinue,
            requestMediaPermissionLauncher = requestMediaPermissionLauncher,
            notificationPermissionLauncher = notificationPermissionLauncher
        )
    }
}

@Composable
fun LoadingIndicator() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun SplashViewComponent(
    permissionState: PermissionsPreferenceState,
    onContinue: () -> Unit,
    requestMediaPermissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    notificationPermissionLauncher: ManagedActivityResultLauncher<String, Boolean>
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp, bottom = 24.dp, start = 10.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Welcome to", fontSize = 48.sp, fontWeight = FontWeight.Bold)
                Row {
                    Text(
                        text = "Oto",
                        fontSize = 50.sp,
                        color = colorResource(id = R.color.colorAccentCyan),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "bit",
                        fontSize = 50.sp,
                        color = colorResource(id = R.color.colorHighlight),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                "Necesitamos permisos para continuar: ",
                fontSize = 25.sp,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                "Necesitamos los siguientes permisos para acceder a tus archivos de música y ofrecerte la mejor experiencia.",
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(18.dp))

            PermissionItem(
                granted = permissionState.mediaPermission,
                title = "Acceso a almacenamiento",
                description = "La aplicación necesita acceso al almacenamiento para encontrar y reproducir tus canciones.",
                iconResId = R.drawable.icon_sd_card,
                onRequest = {
                    val permissionToRequest =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            Manifest.permission.READ_MEDIA_AUDIO
                        } else {
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        }
                    requestMediaPermissionLauncher.launch(permissionToRequest)
                }
            )

            Spacer(modifier = Modifier.height(15.dp))

            PermissionItem(
                granted = permissionState.notificationsPermission,
                title = "Permisos de notificaciones",
                description = "La aplicación necesita permisos para enviar notificaciones.",
                iconResId = R.drawable.icon_notifications,
                onRequest = {
                    val permissionToRequest =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            Manifest.permission.POST_NOTIFICATIONS
                        } else {
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        }
                    notificationPermissionLauncher.launch(permissionToRequest)
                }
            )

            Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo
            ActionButton(
                buttonEnabled = permissionState.mediaPermission && permissionState.notificationsPermission,
                btnColor = colorResource(id = R.color.colorHighlight),
                onContinue
            )
        }
    }
}

@Composable
fun PermissionItem(
    granted: Boolean,
    title: String,
    description: String,
    iconResId: Int,
    onRequest: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                if (!granted) {
                    PermissionButton(iconResId = iconResId, text = "Permitir", onClick = onRequest)
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 18.dp, bottom = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(painterResource(id = iconResId), contentDescription = "Concedido")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Concedido", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = colorResource(R.color.colorAccentPurple))
                    }
                }
            }
        }
    }
}

@Composable
fun ActionButton(buttonEnabled: Boolean, btnColor: Color, onContinue: () -> Unit) {

    val paleColor = btnColor.copy(alpha = 0.6f)

    OutlinedButton(
        onClick = onContinue,
        enabled = buttonEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (buttonEnabled) Color.Transparent else paleColor,
            contentColor = if (buttonEnabled) btnColor else Color.White.copy(alpha = 0.7f),
            disabledContainerColor = paleColor,
            disabledContentColor = Color.White.copy(alpha = 0.7f)
        ),
        border = if (buttonEnabled) ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(btnColor)
        ) else null
    ) {
        Text(
            "¡Vamos!",
            fontSize = 18.sp,
            color = if (buttonEnabled) btnColor else Color.White.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun PermissionButton(iconResId: Int, text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(colorResource(R.color.colorAccentPurple))
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(painterResource(id = iconResId), contentDescription = "icon")
            Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el icono y el texto
            Text(text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

