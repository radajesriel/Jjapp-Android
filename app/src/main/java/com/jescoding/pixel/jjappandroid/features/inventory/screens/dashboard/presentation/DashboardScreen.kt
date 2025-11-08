package com.jescoding.pixel.jjappandroid.features.inventory.screens.dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jescoding.pixel.jjappandroid.features.inventory.screens.dashboard.presentation.components.DashboardHeader
import com.jescoding.pixel.jjappandroid.features.inventory.screens.dashboard.presentation.components.DashboardItem
import com.jescoding.pixel.jjappandroid.features.inventory.screens.dashboard.presentation.data.FakeDashboardData
import com.jescoding.pixel.jjappandroid.shared.theme.JjappAndroidTheme


@Composable
fun DashboardScreen(
    onNavigateToItem: (itemSku: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    DashboardScreenContent(
        uiState = uiState,
        modifier = modifier,
        onNavigateToItem = onNavigateToItem,
        onSearchQueryChanged = {
            viewModel.onSearchQueryChanged(it)
        }
    )
}

@Composable
fun DashboardScreenContent(
    uiState: DashboardUiState,
    onNavigateToItem: (itemSku: String) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val items = uiState.items
    val searchQuery = uiState.searchQuery ?: ""

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            DashboardHeader(
                searchQuery = searchQuery,
                onSearchQueryChanged = onSearchQueryChanged
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Adding key to optimize recompositions (Best practice)
            items(items, key = { it.itemSku }) { item ->
                DashboardItem(
                    data = item,
                    onClick = {
                        onNavigateToItem(item.itemSku)
                    }
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    device = Devices.PIXEL_4
)
@Composable
private fun DashboardScreenPreview() {
    JjappAndroidTheme {
        DashboardScreenContent(
            uiState = DashboardUiState(
                items = FakeDashboardData.items,
                isLoading = false,
                error = null,
                searchQuery = "Search query"
            ),
            onNavigateToItem = {},
            onSearchQueryChanged = {}
        )
    }
}