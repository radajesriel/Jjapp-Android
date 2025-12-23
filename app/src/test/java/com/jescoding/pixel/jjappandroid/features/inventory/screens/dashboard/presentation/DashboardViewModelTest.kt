package com.jescoding.pixel.jjappandroid.features.inventory.screens.dashboard.presentation

import app.cash.turbine.test
import com.jescoding.pixel.jjappandroid.core.DefaultTestClass
import com.jescoding.pixel.jjappandroid.core.data.FakeDashboardData
import com.jescoding.pixel.jjappandroid.core.domain.use_cases.GetDashboardItems
import com.google.common.truth.Truth.assertThat
import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DashboardViewModelTest : DefaultTestClass() {
    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    private lateinit var getDashboardItems: GetDashboardItems
    private lateinit var viewModel: DashboardViewModel

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun `init, should emit loading then success state with items`() = runTest {
        // Arrange
        val items =FakeDashboardData.items
        every { getDashboardItems.invoke() } returns flowOf(items)

        // Act
        viewModel = DashboardViewModel(getDashboardItems, dispatcherProvider)

        // Assert
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertThat(initialState.isLoading).isTrue()
            assertThat(initialState.error).isNull()

            val finalState = awaitItem()
            assertThat(finalState.items).isEqualTo(items)
            assertThat(finalState.isLoading).isFalse()

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `init, throw exception, should emit loading then error state`() = runTest {
        // Arrange
        val flow = flow<List<DashboardItem>> { throw Exception() }
        every { getDashboardItems() } returns flow

        // Act
        viewModel = DashboardViewModel(getDashboardItems, dispatcherProvider)

        // Assert
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertThat(initialState.isLoading).isTrue()
            assertThat(initialState.error).isNull()

            val finalState = awaitItem()
            assertThat(finalState.error).isEqualTo("An unexpected error occurred")
            assertThat(finalState.isLoading).isFalse()

            cancelAndConsumeRemainingEvents()
        }
    }
}