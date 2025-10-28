package com.jescoding.pixel.jjappandroid.features.inventory.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jescoding.pixel.jjappandroid.features.inventory.dashboard.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.features.inventory.dashboard.domain.use_case.GetDashboardItems
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class DashboardUiState(
    val items: List<DashboardItem> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardItems: GetDashboardItems
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadItems()
    }

    private fun loadItems() {
        getDashboardItems()
            .onEach { items ->
                _uiState.value = DashboardUiState(items = items, isLoading = false)
            }
            .launchIn(viewModelScope) // 6. Launch the collection in the ViewModel's scope
    }
}
