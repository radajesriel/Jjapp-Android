package com.jescoding.pixel.jjappandroid.core.data.repository

import com.google.common.truth.Truth.assertThat
import com.jescoding.pixel.jjappandroid.core.DefaultTestClass
import com.jescoding.pixel.jjappandroid.core.data.FakeDashboardData
import com.jescoding.pixel.jjappandroid.core.data.local.real.dao.DashboardDao
import com.jescoding.pixel.jjappandroid.core.domain.repository.DashboardRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DashboardRepositoryImplTest : DefaultTestClass() {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var dao: DashboardDao

    private lateinit var dashboardRepository: DashboardRepository

    @Before
    override fun setUp() {
        super.setUp()
        dashboardRepository = DashboardRepositoryImpl(dao)
    }


    @Test
    fun `getDashboardItems returns flow of dashboard items`() = runTest {
        // Arrange
        val dashboardEntities = FakeDashboardData.entities

        //Mock
        every { dao.getAllItems() } returns flowOf(dashboardEntities)

        // Act
        val result = dashboardRepository.getDashboardItems().first()

        // Assert
        assertThat(2).isEqualTo(result.size)
        assertThat("SKU001").isEqualTo(result[0].itemSku)
        assertThat("Dashboard Item 2").isEqualTo(result[1].itemName)
        verify(exactly = 1) { dao.getAllItems() }
    }

    @Test
    fun `getDashboardItems returns empty flow when no items`() = runTest {
        // Mock
        coEvery { dao.getAllItems() } returns flowOf(emptyList())

        // Act
        val result = dashboardRepository.getDashboardItems().first()

        // Assert
        assertThat(result).isEmpty()
        verify(exactly = 1) { dao.getAllItems() }
    }


    @Test
    fun `getDashboardItemBySku returns item when found`() = runTest {
        //Arrange
        val itemSku = "SKU001"
        val entity = FakeDashboardData.entity

        // Mock
        coEvery { dao.getItemBySku(itemSku) } returns entity

        // Act
        val result = dashboardRepository.getDashboardItemBySku(itemSku)

        //Assert
        assertThat(result).isNotNull()
        assertThat(itemSku).isEqualTo(result?.itemSku)
        assertThat("Dashboard Item 1").isEqualTo(result?.itemName)
        verify { dao.getItemBySku(itemSku) }
    }

    @Test
    fun `getDashboardItemBySku returns null when not found`() = runTest {
        //Arrange
        val itemSKU = "SKU_NOT_EXIST"

        // Mock
        coEvery { dao.getItemBySku(itemSKU) } returns null

        // Act
        val result = dashboardRepository.getDashboardItemBySku(itemSKU)

        //Assert
        assertThat(result).isNull()
        verify { dao.getItemBySku(itemSKU) }
    }

    @Test
    fun `saveDashboardItem calls dao insertItem`() = runTest {
        // Arrange
        val dashboardItem = FakeDashboardData.singleItem

        // Mock
        coEvery { dao.insertItem(any()) } returns Unit

        // Act
        dashboardRepository.saveDashboardItem(dashboardItem)

        // Assert
        coVerify(exactly = 1) { dao.insertItem(any()) }
    }

    @Test
    fun `deleteDashboardItemBySku calls dao deleteItemBySku`() = runTest {
        // Arrange
        val itemSku = "SKU001"

        // Mock
        coEvery { dao.deleteItemBySku(itemSku) } returns Unit

        // Act
        dashboardRepository.deleteDashboardItemBySku(itemSku)

        // Assert
        coVerify { dao.deleteItemBySku(itemSku) }
    }


}