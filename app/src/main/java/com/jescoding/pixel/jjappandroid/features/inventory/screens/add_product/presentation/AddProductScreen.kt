package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_product.presentation

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.jescoding.pixel.jjappandroid.R
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_product.presentation.events.AddProductEvent
import com.jescoding.pixel.jjappandroid.shared.components.SharedTopAppBar
import com.jescoding.pixel.jjappandroid.shared.theme.JjappAndroidTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddProductScreen(
    modifier: Modifier = Modifier,
    viewModel: AddProductViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState()

    // Modern photo picker for Android Tiramisu (API 33+) and above.
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            viewModel.onEvent(AddProductEvent.OnPhotoSelected(uri))
        }
    )

    // Legacy permission and launcher for older Android versions.
    val legacyStoragePermissionState = rememberPermissionState(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    val legacyPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            viewModel.onEvent(AddProductEvent.OnPhotoSelected(uri))
        }
    )

    // Request legacy permission if needed.
    LaunchedEffect(legacyStoragePermissionState.status) {
        if (!legacyStoragePermissionState.status.isGranted
            && legacyStoragePermissionState.status.shouldShowRationale
        ) {
            // Optionally, show a rationale to the user explaining why you need the permission.
        }
    }

    AddProductScreenContent(
        modifier = modifier,
        uiState = uiState.value,
        onNavigateUp = onNavigateUp,
        onEvent = viewModel::onEvent,
        onProductPhotoClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            } else {
                // Handle older Android versions or provide alternative photo picking method
                if (legacyStoragePermissionState.status.isGranted) {
                    legacyPhotoLauncher.launch("image/*")
                } else {
                    legacyStoragePermissionState.launchPermissionRequest()
                }
            }
        }
    )
}

@Composable
fun AddProductScreenContent(
    modifier: Modifier = Modifier,
    uiState: AddProductUiState,
    onNavigateUp: () -> Unit,
    onEvent: (AddProductEvent) -> Unit,
    onProductPhotoClick: () -> Unit
) {
    val itemName = uiState.itemName
    val itemVariant = uiState.itemVariant
    val itemPhotoUri = uiState.itemPhotoUri

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            SharedTopAppBar(
                title = stringResource(R.string.text_label_add_product),
                navigationDescription = stringResource(R.string.icon_label_back),
                onNavigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            ProductPhoto(
                label = "Product Photo*",
                photoUri = itemPhotoUri,
                onClick = onProductPhotoClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProductLabel(
                label = "Product Name*",
                value = itemName,
                placeholder = "Enter product name",
                onValueChange = {
                    onEvent(AddProductEvent.OnNameChange(it))
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProductLabel(
                label = "Variation*",
                value = itemVariant,
                placeholder = "Enter Variation",
                onValueChange = {
                    onEvent(AddProductEvent.OnVariationChange(it))
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProductCosts(
                uiState = uiState,
                onEvent = onEvent
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProductQuantities(
                uiState = uiState,
                onEvent = onEvent
            )
        }
    }
}


@Composable
private fun ProductBaseCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        content = content
    )
}

@Composable
private fun ProductCosts(
    onEvent: (AddProductEvent) -> Unit,
    uiState: AddProductUiState
) {

    val costPrice = uiState.costPriceDisplay
    val sellingPrice = uiState.sellingPriceDisplay

    val placeholder = "Set"

    ProductBaseCard {
        Column {
            ProductRow(
                label = "Product Cost",
                value = costPrice,
                placeholder = placeholder,
                onValueChange = {
                    onEvent(AddProductEvent.OnCostChange(it))
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 12.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .3f)
            )

            ProductRow(
                label = "Selling Price",
                value = sellingPrice,
                placeholder = placeholder,
                onValueChange = {
                    onEvent(AddProductEvent.OnSellingPriceChange(it))
                }
            )
        }
    }
}

@Composable
private fun ProductQuantities(
    onEvent: (AddProductEvent) -> Unit,
    uiState: AddProductUiState
) {
    val availableStock = uiState.availableStock
    val onHandStock = uiState.onHandStock
    val onTheWayStock = uiState.onTheWayStock

    val placeholder = "0"

    ProductBaseCard {
        Column {
            ProductRow(
                label = "Available Stock",
                value = availableStock,
                placeholder = placeholder,
                onValueChange = {
                    onEvent(AddProductEvent.OnAvailableStockChange(it))
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 12.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .3f)
            )

            ProductRow(
                label = "On Hand Stock",
                value = onHandStock,
                placeholder = placeholder,
                onValueChange = {
                    onEvent(AddProductEvent.OnHandStockChange(it))
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 12.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .3f)
            )

            ProductRow(
                label = "On The Way Stock",
                value = onTheWayStock,
                placeholder = placeholder,
                onValueChange = {
                    onEvent(AddProductEvent.OnTheWayStockChange(it))
                }
            )
        }
    }
}

@Composable
private fun ProductRow(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f),
            text = label,
            style = MaterialTheme.typography.titleMedium
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .weight(1f),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                textAlign = TextAlign.End
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.End
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}


@Composable
private fun ProductPhoto(
    label: String,
    photoUri: Uri?,
    onClick: () -> Unit
) {
    ProductBaseCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium
            )

            val imageModifier = Modifier
                .size(100.dp)
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(8.dp))
                .align(Alignment.Start)
                .clickable {
                    onClick()
                }

            if (photoUri != null) {
                AsyncImage(
                    modifier = imageModifier,
                    model = photoUri,
                    contentDescription = "Product Photo",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.outline_add_photo_alternate_24)
                )
            } else {
                Image(
                    modifier = imageModifier,
                    contentDescription = "Product Photo",
                    painter = painterResource(R.drawable.outline_add_photo_alternate_24),
                )
            }
        }
    }
}


@Composable
private fun ProductLabel(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {

    ProductBaseCard {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddProductScreenPreview() {
    JjappAndroidTheme {
        AddProductScreenContent(
            uiState = AddProductUiState(),
            onNavigateUp = {},
            onEvent = {},
            onProductPhotoClick = {}
        )
    }
}