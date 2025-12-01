package com.jescoding.pixel.jjappandroid.features.inventory.screens.item.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jescoding.pixel.jjappandroid.core.domain.use_cases.GetDashboardItemBySku
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class ItemScreenViewModel @Inject constructor(
    private val getDashboardItemBySku: GetDashboardItemBySku,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val itemSku: String = checkNotNull(savedStateHandle["itemSku"])
    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    init {
        fetchItemBySku(itemSku)
    }


    private fun fetchItemBySku(sku: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {
                val item = getDashboardItemBySku(sku)

                if (item != null) {
                    _uiState.update { it.copy(item = item) }
                } else {
                    _uiState.update { it.copy(error = "Item with SKU $sku not found.") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "An unexpected error occurred.") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }

        }
    }
}