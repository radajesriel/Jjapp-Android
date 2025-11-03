package com.jescoding.pixel.jjappandroid.features.inventory.item.presentation

import androidx.lifecycle.ViewModel
import com.jescoding.pixel.jjappandroid.features.inventory.item.domain.model.InventoryItem
import com.jescoding.pixel.jjappandroid.features.inventory.item.presentation.data.FakeInventoryData
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class InventoryUiState(
    val items: List<InventoryItem> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ItemScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = InventoryUiState(
            items = FakeInventoryData.items
        )
    }

}