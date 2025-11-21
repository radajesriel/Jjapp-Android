package com.jescoding.pixel.jjappandroid.features.inventory.screens.item.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jescoding.pixel.jjappandroid.R
import com.jescoding.pixel.jjappandroid.features.inventory.screens.item.presentation.component.InventoryBody
import com.jescoding.pixel.jjappandroid.features.inventory.screens.item.presentation.component.InventoryItemHeader
import com.jescoding.pixel.jjappandroid.shared.components.SharedTopAppBar
import com.jescoding.pixel.jjappandroid.shared.theme.JjappAndroidTheme

@Composable
fun ItemScreen(
    onNavigateUp: () -> Unit,
    onEditClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ItemScreenViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val itemSku = state.item?.itemSku

    ItemScreenContent(
        modifier = modifier,
        uiState = state,
        onNavigateUp = onNavigateUp,
        onEditClicked = {
            onEditClicked()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreenContent(
    onNavigateUp: () -> Unit,
    onEditClicked: () -> Unit,
    modifier: Modifier = Modifier,
    uiState: InventoryUiState
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            SharedTopAppBar(
                title = stringResource(R.string.text_label_sku_details),
                navigationDescription = stringResource(R.string.icon_label_back),
                onNavigateUp = onNavigateUp
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEditClicked()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Product",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
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

@Preview(showBackground = true)
@Composable
fun ItemScreenContentPreview() {
    JjappAndroidTheme {
        ItemScreenContent(
            uiState = InventoryUiState(),
            onNavigateUp = {},
            onEditClicked = {}
        )
    }
}
