package com.jescoding.pixel.jjappandroid.features.inventory.screens.item.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.jescoding.pixel.jjappandroid.core.DefaultTestClass
import com.jescoding.pixel.jjappandroid.core.data.FakeDashboardData
import com.jescoding.pixel.jjappandroid.core.domain.use_cases.GetDashboardItemBySku
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ItemScreenViewModelTest : DefaultTestClass(){

    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    private lateinit var getDashboardItemBySku: GetDashboardItemBySku
    private lateinit var itemScreenViewModel: ItemScreenViewModel

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun `init, when item exists, should emit loading then success state with item`() = runTest {
        // Arrange
        val itemSku = "SKU001"
        val savedStateHandle = SavedStateHandle().apply { set("itemSku", itemSku) }

        // Arrange
        val item = FakeDashboardData.singleItem

        coEvery { getDashboardItemBySku(itemSku) } returns item

        // Act
        itemScreenViewModel = ItemScreenViewModel(getDashboardItemBySku, savedStateHandle)

        // Assert
        itemScreenViewModel.uiState.test {
            // 1. Await loading state
            val finalState = awaitItem()
            assertThat(finalState.item).isEqualTo(item)
            assertThat(finalState.isLoading).isFalse()
            cancelAndConsumeRemainingEvents()
        }
    }
}