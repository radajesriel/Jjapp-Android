package com.jescoding.pixel.jjappandroid.features.inventory.screens.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.core.domain.providers.DispatcherProvider
import com.jescoding.pixel.jjappandroid.core.domain.use_cases.GetDashboardItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val items: List<DashboardItem> = emptyList(),
    val searchQuery: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardItems: GetDashboardItems,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _originalItems = MutableStateFlow<List<DashboardItem>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        observeStateChanges()
        loadItems()
    }

    private fun observeStateChanges() {
        combine(
            _originalItems,
            _searchQuery,
            _isLoading,
            _error
        ) { items, query, isLoading, error ->
            val filteredItems = if (query.isBlank()) {
                items
            } else {
                items.filter { item ->
                    item.itemName.contains(query, ignoreCase = true) ||
                            item.itemSku.contains(query, ignoreCase = true)
                }
            }
            DashboardUiState(
                items = filteredItems,
                searchQuery = query,
                isLoading = isLoading,
                error = error
            )
        }.onEach {
            _uiState.value = it
        }.launchIn(viewModelScope)
    }

    private fun loadItems() {
        getDashboardItems()
            .flowOn(dispatcherProvider.io)
            .onStart {
                _isLoading.value = true
                _error.value = null
            }
            .onEach { items ->
                _originalItems.value = items
                _isLoading.value = false
            }
            .catch { cause ->
                _error.value = "An unexpected error occurred"
                _isLoading.value = false
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}
