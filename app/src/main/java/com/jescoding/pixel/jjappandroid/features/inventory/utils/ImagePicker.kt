package com.jescoding.pixel.jjappandroid.features.inventory.utils

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberImagePicker(onImageSelected: (Uri?) -> Unit): () -> Unit {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = onImageSelected
    )

    // Legacy permission and launcher
    val legacyPermissionState = rememberPermissionState(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val legacyPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = onImageSelected
    )

    LaunchedEffect(legacyPermissionState.status) {
        if (!legacyPermissionState.status.isGranted
            && legacyPermissionState.status.shouldShowRationale
        ) {
            // Optionally, show a rationale to the user explaining why you need the permission.
        }
    }

    // This is the function that will be returned and called on click
    return {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        } else {
            if (legacyPermissionState.status.isGranted) {
                legacyPhotoLauncher.launch("image/*")
            } else {
                legacyPermissionState.launchPermissionRequest()
            }
        }
    }
}