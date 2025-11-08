package com.jescoding.pixel.jjappandroid.features.inventory.screens.item.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jescoding.pixel.jjappandroid.R
import com.jescoding.pixel.jjappandroid.features.inventory.screens.item.presentation.component.InventoryBody
import com.jescoding.pixel.jjappandroid.features.inventory.screens.item.presentation.component.InventoryItemHeader
import com.jescoding.pixel.jjappandroid.shared.theme.JjappAndroidTheme

@Composable
fun ItemScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ItemScreenViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    ItemScreenContent(
        modifier = modifier,
        uiState = state,
        onNavigateUp = onNavigateUp
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreenContent(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    uiState: InventoryUiState
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            ItemScreenTopAppBar(onNavigateUp = onNavigateUp)
        }
    ) { innerPadding ->
        when {
            uiState.item != null -> {
                val item = uiState.item
                Column(
                    modifier = modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    InventoryItemHeader(data = item)
                    InventoryBody(data = item)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemScreenTopAppBar(onNavigateUp: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.text_label_sku_details),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White
        ),
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.icon_label_back),
                    tint = Color.White
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ItemScreenContentPreview() {
    JjappAndroidTheme {
        ItemScreenContent(
            uiState = InventoryUiState(),
            onNavigateUp = {}
        )
    }
}
