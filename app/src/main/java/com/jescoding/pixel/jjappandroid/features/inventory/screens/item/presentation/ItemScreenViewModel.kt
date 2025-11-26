package com.jescoding.pixel.jjappandroid.features.inventory.screens.item.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.core.domain.providers.DispatcherProvider
import com.jescoding.pixel.jjappandroid.core.domain.use_cases.GetDashboardItemBySku
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private val _item = MutableStateFlow<DashboardItem?>(null)
    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)
    private val itemSku: String = checkNotNull(savedStateHandle["itemSku"])
    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    init {
        observeStateChanges()
        fetchItemBySku(itemSku)
    }

    private fun observeStateChanges() {
        combine(
            _item,
            _isLoading,
            _error
        ) { item, isLoading, error ->
            InventoryUiState(
                item = item,
                isLoading = isLoading,
                error = error
            )
        }.onEach {
            _uiState.value = it
        }.launchIn(viewModelScope)
    }

    private fun fetchItemBySku(sku: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val item = getDashboardItemBySku(sku)

                if (item != null) {
                    _item.value = item
                } else {
                    _error.value = "Item with SKU $sku not found."
                }
            } catch (e: Exception) {
                _error.value = "An unexpected error occurred."
            } finally {
                _isLoading.value = false
            }

        }
    }
}