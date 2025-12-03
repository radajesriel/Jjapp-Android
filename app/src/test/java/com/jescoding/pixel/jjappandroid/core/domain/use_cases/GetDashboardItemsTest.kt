package com.jescoding.pixel.jjappandroid.core.domain.use_cases

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.jescoding.pixel.jjappandroid.core.DefaultTestClass
import com.jescoding.pixel.jjappandroid.core.data.FakeDashboardData
import com.jescoding.pixel.jjappandroid.core.domain.repository.DashboardRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class GetDashboardItemsTest : DefaultTestClass(){
    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var dashboardRepository: DashboardRepository
    private lateinit var getDashboardItems: GetDashboardItems

    @Before
    override fun setUp() {
        super.setUp()
        getDashboardItems = GetDashboardItems(dashboardRepository)
    }

    @Test
    fun `invoke() should return flow of dashboard items from repository`() = runTest {
        val mockDashboardItems = FakeDashboardData.items

        coEvery {
            dashboardRepository.getDashboardItems()
        } returns flowOf(mockDashboardItems)

        getDashboardItems().test {
            val emittedList = awaitItem()
            assertThat(emittedList).isEqualTo(mockDashboardItems)
            assertThat(emittedList).hasSize(FakeDashboardData.items.size)
            awaitComplete()
        }
    }

    @Test
    fun `invoke() should return an empty list when repository is empty`() = runTest {
        // Arrange: Mock the repository to return a flow with an empty list
        coEvery {
            dashboardRepository.getDashboardItems()
        } returns flowOf(emptyList())

        // Act & Assert
        getDashboardItems().test {
            val emittedList = awaitItem()
            assertThat(emittedList).isNotNull()
            assertThat(emittedList).isEmpty()
            awaitComplete()
        }
    }

    @Test
    fun `invoke() should emit an error when repository throws exception`() = runTest {
        // Arrange: Mock the repository to return a flow that throws an error
        val testException = IOException("Database error")
        coEvery {
            dashboardRepository.getDashboardItems()
        } returns flow { throw testException }

        // Act & Assert
        getDashboardItems().test {
            // Use awaitError() from Turbine to catch the exception
            val emittedError = awaitError()

            assertThat(emittedError).isInstanceOf(IOException::class.java)
            assertThat(emittedError.message).isEqualTo("Database error")
            // No awaitComplete() is called, as the flow terminates with an error
        }
    }
}