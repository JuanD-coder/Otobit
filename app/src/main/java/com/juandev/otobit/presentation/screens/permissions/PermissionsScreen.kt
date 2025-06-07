package com.juandev.otobit.presentation.screens.permissions

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PermissionsScreen(
    viewModel: PermissionsViewModel = hiltViewModel(),
    onContinue: () -> Unit,
) {

    val currentPermissions by viewModel.permissionsStatus.collectAsState()
    val context = LocalContext.current

    // --- LANZADORES DE PERMISOS ---
    val requestMediaPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.updateMediaPermissionStatus(isGranted)
            // Opcional: También puedes actualizar legacyRead si es relevante para versiones antiguas
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                viewModel.updateLegacyReadStatus(isGranted) // Si usas el mismo permiso para legacy
            }
        }
    )

    // Para ACTION_OPEN_DOCUMENT_TREE (SAF)
    // Esto es un ejemplo, podrías necesitar ajustar el contrato y el manejo del resultado
    val openDocumentTreeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree(),
        onResult = { uri ->
            val isGranted = uri != null
            viewModel.updateFolderAccessStatus(isGranted)
            if (isGranted) {
                // Guarda el URI de forma persistente si es necesario (DataStore o SharedPreferences)
                // para acceder a la carpeta más tarde. Esto es importante para SAF.
                // context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                // viewModel.saveFolderUri(uri.toString()) // Ejemplo
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Estado Actual (Desde DataStore):")
            Text(text = "  Permiso de Medios (Audio): ${currentPermissions.mediaPermission}")
            Text(text = "  Acceso a Carpetas (SAF): ${currentPermissions.folderAccess}")
            Text(text = "  Lectura Legacy (SDK < 33): ${currentPermissions.legacyRead}") // Aclarar su uso

            Spacer(modifier = Modifier.height(24.dp))

            Text("Permisos Requeridos", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Necesitamos los siguientes permisos para acceder a tus archivos de música y ofrecerte la mejor experiencia.")
            Spacer(modifier = Modifier.height(24.dp))

            PermissionItem(
                granted = currentPermissions.mediaPermission,
                title = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    "Acceso a archivos de audio"
                } else {
                    "Acceso a almacenamiento (audio)"
                },
                description = "Permite a la app encontrar y reproducir tus canciones.",
                onRequest = {
                    val permissionToRequest =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            Manifest.permission.READ_MEDIA_AUDIO
                        } else {
                            Manifest.permission.READ_EXTERNAL_STORAGE // Para versiones antiguas
                        }
                    requestMediaPermissionLauncher.launch(permissionToRequest)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PermissionItem(
                granted = currentPermissions.folderAccess,
                title = "Seleccionar carpeta de música",
                description = "Elige la carpeta donde guardas tu música para un acceso directo.",
                onRequest = {
                    openDocumentTreeLauncher.launch(null) // Puedes pasar un URI inicial si es necesario
                }
            )

            // Considera si el permiso de "Lectura Legacy" necesita su propio PermissionItem
            // o si está cubierto por el de "Acceso a audio del dispositivo" en versiones antiguas.
            // Si es READ_EXTERNAL_STORAGE para SDK < 33 y es el mismo que el de audio,
            // ya lo estás manejando. Si es para otra cosa, añade otro PermissionItem.
        }

        Button(
            onClick = onContinue,
            // Habilita si los permisos principales están concedidos.
            // La necesidad de folderAccess puede depender de si el usuario *debe* seleccionar una carpeta
            // o si es opcional y la app puede buscar en todo el almacenamiento multimedia.
            enabled = currentPermissions.mediaPermission, // O (currentPermissions.mediaPermission || currentPermissions.legacyRead) si legacy es una alternativa válida
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¡Vamos!")
        }
    }
}

@Composable
fun PermissionItem(
    granted: Boolean,
    title: String,
    description: String,
    onRequest: () -> Unit
) {
    Card( // Usar Card para un mejor agrupamiento visual
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Checkbox(
                checked = granted,
                onCheckedChange = null, // El Checkbox es solo visual
                enabled = false, // No interactuable directamente
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    //disabledCheckedColor = MaterialTheme.colorScheme.primary.copy(alpha = ContentA), // Para tema oscuro/claro
                    //disabledUncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = androidx.wear.compose.material.ContentAlpha.disabled)
                )
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(description, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.width(16.dp))
            if (!granted) { // Solo muestra el botón si el permiso no está concedido
                OutlinedButton(onClick = onRequest) {
                    Text("Permitir")
                }
            } else {
                // Opcional: Mostrar un Icono o texto de "Concedido" si se desea
                Text("Concedido", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}