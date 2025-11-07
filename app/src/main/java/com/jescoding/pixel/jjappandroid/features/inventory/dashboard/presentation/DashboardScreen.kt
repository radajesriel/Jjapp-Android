package com.jescoding.pixel.jjappandroid.features.inventory.dashboard.presentation

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jescoding.pixel.jjappandroid.features.inventory.dashboard.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.features.inventory.dashboard.presentation.components.DashboardHeader
import com.jescoding.pixel.jjappandroid.features.inventory.dashboard.presentation.components.DashboardItem
import com.jescoding.pixel.jjappandroid.features.inventory.dashboard.presentation.data.FakeDashboardData
import com.jescoding.pixel.jjappandroid.shared.theme.JjappAndroidTheme


@Composable
fun DashboardScreen(
    onNavigateToItem: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val state = viewModel.uiState.collectAsState()

    DashboardScreenContent(
        items = state.value.items,
        searchQuery = searchQuery,
        onSearchQueryChanged = { newQuery -> searchQuery = newQuery },
        modifier = modifier,
        onNavigateToItem = onNavigateToItem
    )
}

@Composable
fun DashboardScreenContent(
    onNavigateToItem: () -> Unit,
    items: List<DashboardItem>,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
            items(items) { item ->
                // Assuming you have a DashboardItem composable
                // If not, you'd have your item UI here
                DashboardItem(
                    data = item,
                    onClick = onNavigateToItem
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
            items = FakeDashboardData.items,
            searchQuery = "Search query",
            onSearchQueryChanged = {},
            onNavigateToItem = {}
        )
    }
}