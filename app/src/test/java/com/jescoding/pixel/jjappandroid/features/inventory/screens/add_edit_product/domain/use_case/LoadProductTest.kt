package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.use_case

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
class LoadProductTest : DefaultTestClass() {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var dashboardRepository: DashboardRepository
    private lateinit var loadProduct: LoadProduct

    @Before
    override fun setUp() {
        super.setUp()
        loadProduct = LoadProduct(
            dashboardRepository,
            dispatcherProvider
        )
    }

    @Test
    fun `invoke() should return DashboardItem`() = runTest {
        //Arrange
        val testSku = "SKU001"
        val expectedItem = FakeDashboardData.singleItem
        //Mock
        coEvery {
            dashboardRepository.getDashboardItemBySku(testSku)
        } returns FakeDashboardData.singleItem
        //Act
        val result = loadProduct(testSku)
        //Assert
        assertThat(result).isEqualTo(expectedItem)
    }

    @Test
    fun `invoke() then repository throws Exception should propagate the exception`() = runTest {
        //Arrange
        val testSku = "TESTSKU_IO_EXCEPTION"
        //Mock
        coEvery { dashboardRepository.getDashboardItemBySku(testSku) } throws IOException("IO")
        //Act
        try {
            loadProduct(testSku)
            throw AssertionError("Expected an IOException to be thrown")
        } catch (e: IOException) {
            //Assert
            assertThat(e).isInstanceOf(IOException::class.java)
            assertThat(e.message).isEqualTo("IO")
        }
    }

}