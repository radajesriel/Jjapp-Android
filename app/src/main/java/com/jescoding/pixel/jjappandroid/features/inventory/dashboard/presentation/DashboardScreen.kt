package com.jescoding.pixel.jjappandroid.features.inventory.dashboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jescoding.pixel.jjappandroid.features.inventory.dashboard.presentation.components.DashboardHeader
import com.jescoding.pixel.jjappandroid.shared.theme.JjappAndroidTheme


@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        DashboardHeader(
            searchQuery = searchQuery,
            onSearchQueryChanged = { newQuery -> searchQuery = newQuery }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val itemsList = (1..20).map { "Inventory Item $it" }

            items(itemsList) { item ->
                Text(text = item)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    JjappAndroidTheme {
        DashboardScreen()
    }
}