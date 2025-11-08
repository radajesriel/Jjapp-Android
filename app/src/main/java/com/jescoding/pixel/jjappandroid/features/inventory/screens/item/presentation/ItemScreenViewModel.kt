package com.jescoding.pixel.jjappandroid.features.inventory.screens.item.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.features.inventory.domain.use_cases.GetDashboardItemBySku
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

data class InventoryUiState(
    val item: DashboardItem? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

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
        var wasItemEmitted = false
        getDashboardItemBySku(sku)
            .onEach { foundItem ->
                // Use update extension since this is the best practice for updating state flows
                // It ensures thread-safety and consistency
                wasItemEmitted = true
                _uiState.update { state ->
                    state.copy(
                        item = foundItem,
                        isLoading = false
                    )
                }
            }.catch { exception ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = "An unexpected error occurred."
                    )
                }
            }.onCompletion { cause ->
                if (cause == null && !wasItemEmitted) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Item with SKU $sku not found."
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

}