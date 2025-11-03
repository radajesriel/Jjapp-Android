package com.jescoding.pixel.jjappandroid.features.inventory.item.presentation

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jescoding.pixel.jjappandroid.R
import com.jescoding.pixel.jjappandroid.features.inventory.item.domain.model.InventoryItem
import com.jescoding.pixel.jjappandroid.features.inventory.item.presentation.component.InventoryBody
import com.jescoding.pixel.jjappandroid.features.inventory.item.presentation.component.InventoryItemHeader
import com.jescoding.pixel.jjappandroid.features.inventory.item.presentation.data.FakeInventoryData
import com.jescoding.pixel.jjappandroid.shared.theme.JjappAndroidTheme

@Composable
fun ItemScreen(
    modifier: Modifier = Modifier,
    viewModel: ItemScreenViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState()
    val item = state.value.items.first()

    ItemScreenContent(
        modifier = modifier,
        item = item
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreenContent(
    modifier: Modifier = Modifier,
    item: InventoryItem,
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
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
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.icon_label_back),
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
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

@Preview(showBackground = true)
@Composable
fun ItemScreenContentPreview() {
    JjappAndroidTheme {
        ItemScreenContent(
            item = FakeInventoryData.items.first()
        )
    }
}
