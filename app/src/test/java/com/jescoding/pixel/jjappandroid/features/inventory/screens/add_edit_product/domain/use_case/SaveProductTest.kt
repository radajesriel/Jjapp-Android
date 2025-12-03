package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.use_case

import android.net.Uri
import com.google.common.truth.Truth.assertThat
import com.jescoding.pixel.jjappandroid.core.DefaultTestClass
import com.jescoding.pixel.jjappandroid.core.data.FakeDashboardData
import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.core.domain.repository.DashboardRepository
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.model.NewProductInput
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class SaveProductTest : DefaultTestClass() {

    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    private lateinit var dashboardRepository: DashboardRepository
    @MockK
    private lateinit var getPermanentImageUri: GetPermanentImageUri
    private lateinit var saveProduct: SaveProduct

    @Before
    override fun setUp() {
        super.setUp()
        saveProduct = SaveProduct(
            dashboardRepository,
            getPermanentImageUri,
            dispatcherProvider
        )
    }

    @Test
    fun `invoke() with new image should get permanent uri and save product`() = runTest {
        // Arrange
        val tempUri = mockk<Uri>()
        val permanentUri = mockk<Uri>()
        val productInput = NewProductInput(
            itemSku = "SKU001",
            itemName = "Dashboard Item 1",
            itemVariant = "dashboard_item_1_variant",
            availableStock = "10",
            onHandStock = "20",
            onTheWayStock = "30",
            itemCostPrice = "120.00",
            itemSellingPrice = "150.00",
            itemImageResId = 0,
            itemUri = tempUri
        )
        val dashboardItemSlot = slot<DashboardItem>()

        // Mock dependencies
        coEvery { getPermanentImageUri.invoke(tempUri) } returns permanentUri
        coEvery { dashboardRepository.saveDashboardItem(capture(dashboardItemSlot)) } returns Unit

        // Act
        saveProduct(productInput)

        // Assert Verify dependent use case was called
        coVerify { getPermanentImageUri.invoke(tempUri) }

        // Verify repository was called
        coVerify { dashboardRepository.saveDashboardItem(any()) }

        // Assert on the captured DashboardItem
        val capturedItem = dashboardItemSlot.captured
        assertThat(capturedItem.itemName).isEqualTo("Dashboard Item 1")
        assertThat(capturedItem.itemVariant).isEqualTo("dashboard_item_1_variant")
        assertThat(capturedItem.itemUri).isEqualTo(permanentUri) // Check that the permanent URI was used
        assertThat(capturedItem.itemCostPrice.toString()).isEqualTo("120.00")
        assertThat(capturedItem.itemSellingPrice.toString()).isEqualTo("150.00")
    }

    @Test
    fun `invoke() without an image should save product with null uri`() = runTest {
        // Arrange
        val productInput = NewProductInput(
            itemSku = "SKU001",
            itemName = "Dashboard Item 1",
            itemVariant = "dashboard_item_1_variant",
            availableStock = "10",
            onHandStock = "20",
            onTheWayStock = "30",
            itemCostPrice = "120.00",
            itemSellingPrice = "150.00",
            itemImageResId = 0,
            itemUri = null
        )
        val dashboardItemSlot = slot<DashboardItem>()

        // Mock repository (no need to mock getPermanentImageUri as it shouldn't be called)
        coEvery { dashboardRepository.saveDashboardItem(capture(dashboardItemSlot)) } returns Unit

        // Act
        saveProduct(productInput)

        // Assert Verify that the getPermanentImageUri use case was NEVER called
        coVerify(exactly = 0) { getPermanentImageUri.invoke(any()) }

        // Verify the repository was called
        coVerify(exactly = 1) { dashboardRepository.saveDashboardItem(any()) }

        // Assert that the item URI in the saved object is null
        val capturedItem = dashboardItemSlot.captured
        assertThat(capturedItem.itemName).isEqualTo("Dashboard Item 1")
        assertThat(capturedItem.itemUri).isNull()
    }

    @Test
    fun `invoke() when repository throws exception should propagate exception`() = runTest {
        // Arrange
        val productInput = NewProductInput(
            itemSku = "SKU001",
            itemName = "Dashboard Item 1",
            itemVariant = "dashboard_item_1_variant",
            availableStock = "10",
            onHandStock = "20",
            onTheWayStock = "30",
            itemCostPrice = "120.00",
            itemSellingPrice = "150.00",
            itemImageResId = 0,
            itemUri = null
        )
        val testException = IOException("Failed to write to database")

        // Mock repository to throw an error
        coEvery { dashboardRepository.saveDashboardItem(any()) } throws testException

        // Act & Assert
        try {
            saveProduct(productInput)
            // If this line is reached, the test fails
            throw AssertionError("Expected an IOException to be thrown")
        } catch (e: IOException) {
            // Success: The correct exception was caught
            assertThat(e).isInstanceOf(IOException::class.java)
            assertThat(e.message).isEqualTo("Failed to write to database")
        }

        // Verify that other use cases (if any) were still called
        coVerify(exactly = 0) { getPermanentImageUri.invoke(any()) }
    }

}