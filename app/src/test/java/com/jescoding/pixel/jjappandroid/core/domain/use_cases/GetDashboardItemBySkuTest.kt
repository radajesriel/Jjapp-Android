package com.jescoding.pixel.jjappandroid.core.domain.use_cases

import com.google.common.truth.Truth.assertThat
import com.jescoding.pixel.jjappandroid.core.DefaultTestClass
import com.jescoding.pixel.jjappandroid.core.data.FakeDashboardData
import com.jescoding.pixel.jjappandroid.core.domain.repository.DashboardRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class GetDashboardItemBySkuTest : DefaultTestClass() {

    @get:Rule
    val mockkRule = MockKRule(this) //Should not be private

    @MockK
    private lateinit var dashboardRepository: DashboardRepository
    private lateinit var getDashboardItemBySku: GetDashboardItemBySku

    @Before
    override fun setUp() {
        super.setUp()
        getDashboardItemBySku = GetDashboardItemBySku(
            dashboardRepository,
            dispatcherProvider
        )
    }

    @Test
    fun `invoke() with existing sku should return correct dashboard item`() = runTest {
        // Arrange
        val testSku = "SKU001"
        val expectedItem = FakeDashboardData.singleItem

        // Mock the repository's suspend function to return the expected item
        coEvery { dashboardRepository.getDashboardItemBySku(testSku) } returns expectedItem

        // Act
        val result = getDashboardItemBySku(testSku)

        // Assert
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(expectedItem)
        assertThat(result?.itemSku).isEqualTo(testSku)
    }

    @Test
    fun `invoke() with non-existing sku should return null`() = runTest {
        // Arrange
        val testSku = "SKU-DOES-NOT-EXIST"
        coEvery { dashboardRepository.getDashboardItemBySku(testSku) } returns null
        // Act
        val result = getDashboardItemBySku(testSku)
        // Assert
        assertThat(result).isNull()
    }

    @Test
    fun `invoke() when repository throws exception should propagate the exception`() = runTest {
        // Arrange
        val testSku = "SKU-ERROR"
        val testException = IOException("Database connection failed")
        coEvery { dashboardRepository.getDashboardItemBySku(testSku) } throws testException

        // Act & Assert
        try {
            getDashboardItemBySku(testSku)
            // If this line is reached, the test fails.
            throw AssertionError("Expected an IOException to be thrown")
        } catch (e: IOException) {
            // Success: The correct exception was caught.
            assertThat(e).isInstanceOf(IOException::class.java)
            assertThat(e.message).isEqualTo("Database connection failed")
        }
    }


}