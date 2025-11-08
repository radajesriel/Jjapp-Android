package com.jescoding.pixel.jjappandroid.features.inventory.screens.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.features.inventory.domain.use_cases.GetDashboardItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class DashboardUiState(
    val items: List<DashboardItem> = emptyList(),
    val searchQuery: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardItems: GetDashboardItems
) : ViewModel() {

    private var originalItems: List<DashboardItem> = emptyList()

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadItems()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }

        val filteredList = if (query.isBlank()) {
            originalItems
        } else {
            originalItems.filter { item ->
                item.itemName.contains(query, ignoreCase = true) ||
                        item.itemSku.contains(query, ignoreCase = true)
            }
        }
        _uiState.update { it.copy(items = filteredList) }
    }

    private fun loadItems() {
        getDashboardItems()
            .onEach { items ->
                originalItems = items
                _uiState.update { it.copy(items = items, isLoading = false) }
            }
            .catch { cause ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = "An unexpected error occurred."
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}
