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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class DashboardUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val error: String? = null,
    private val originalItems: List<DashboardItem> = emptyList()
) {
    val items: List<DashboardItem>
        get() = if (searchQuery.isBlank()) {
            originalItems
        } else {
            originalItems.filter { item ->
                item.itemName.contains(searchQuery, ignoreCase = true) ||
                        item.itemSku.contains(searchQuery, ignoreCase = true)
            }
        }

}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardItems: GetDashboardItems,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadItems()
    }


    private fun loadItems() {
        getDashboardItems()
            .flowOn(dispatcherProvider.io)
            .onStart {
                _uiState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
            .onEach { items ->
                _uiState.update {
                    it.copy(
                        originalItems = items,
                        isLoading = false,
                    )
                }
            }
            .catch { cause ->
                _uiState.update {
                    it.copy(
                        error = "An unexpected error occurred",
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }
}
