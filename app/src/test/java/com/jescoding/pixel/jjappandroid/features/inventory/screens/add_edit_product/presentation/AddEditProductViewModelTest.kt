package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.presentation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.jescoding.pixel.jjappandroid.core.DefaultTestClass
import com.jescoding.pixel.jjappandroid.core.data.FakeDashboardData
import com.jescoding.pixel.jjappandroid.core.domain.providers.ResourceProvider
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.use_case.DeleteProduct
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.use_case.LoadProduct
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.use_case.SaveProduct
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.presentation.events.AddEditProductEvent
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.presentation.events.AddEditProductSideEffect
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddEditProductViewModelTest : DefaultTestClass() {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK(relaxed = true)
    private lateinit var saveProduct: SaveProduct

    @MockK
    private lateinit var loadProduct: LoadProduct

    @MockK(relaxed = true)
    private lateinit var deleteProduct: DeleteProduct

    @MockK
    private lateinit var tempResourceProvider: ResourceProvider
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: AddEditProductViewModel

    @Before
    override fun setUp() {
        super.setUp()
        savedStateHandle = SavedStateHandle()
    }

    /**
     * Cannot observe/assert changes in the UI State,
     * only the final state is being returned probably because its not emitted
     * one by one, just assert the final ui state
     */
    @Test
    fun `onInitializeViewModel, call load product`() = runTest {
        // Arrange
        val itemSku = "SKU001"
        savedStateHandle["itemSku"] = itemSku

        // Arrange
        val dashboardItem = FakeDashboardData.singleItem
        coEvery { loadProduct(itemSku) } returns dashboardItem

        // Act
        viewModel = AddEditProductViewModel(
            saveProduct,
            loadProduct,
            deleteProduct,
            resourceProvider,
            savedStateHandle
        )

        // Assert UI State
        viewModel.uiState.test {
            val finalState = awaitItem()
            assertThat(finalState.isLoading).isFalse()
            assertThat(finalState.itemSku).isEqualTo(itemSku)
            assertThat(finalState.itemName).isEqualTo("Dashboard Item 1")
            cancelAndConsumeRemainingEvents()
        }

        // Assert that the use case was called
        coVerify(exactly = 1) { loadProduct(itemSku) }
    }

    @Test
    fun `onInitializeViewModel, throwsException`() = runTest {
        // Arrange
        val itemSku = "SKU001"
        savedStateHandle["itemSku"] = itemSku

        // Arrange
        coEvery { loadProduct(itemSku) } throws Exception()

        // Act
        viewModel = AddEditProductViewModel(
            saveProduct,
            loadProduct,
            deleteProduct,
            resourceProvider,
            savedStateHandle
        )

        // Assert UI state
        viewModel.uiState.test {
            val finalState = awaitItem()
            assertThat(finalState.isLoading).isFalse()
            assertThat(finalState.error).isEqualTo("Unexpected error occurred")
        }

        // Assert load product is called
        coVerify { loadProduct(itemSku) }
    }

    @Test
    fun `onInitializeViewModel, null item sku, should return correct header`() = runTest {
        // Arrange
        savedStateHandle["itemSku"] = null

        // Arrange
        coEvery {
            tempResourceProvider.getString(
                com.jescoding.pixel.jjappandroid.R.string.text_label_add_product
            )
        } returns "Add Product"

        coEvery {
            tempResourceProvider.getString(
                com.jescoding.pixel.jjappandroid.R.string.button_label_save_product
            )
        } returns "Save Product"


        // Act
        viewModel = AddEditProductViewModel(
            saveProduct,
            loadProduct,
            deleteProduct,
            tempResourceProvider,
            savedStateHandle
        )

        // Assert UI State
        viewModel.uiState.test {
            val finalState = awaitItem()
            assertThat(finalState.header).isEqualTo("Add Product")
            assertThat(finalState.buttonLabel).isEqualTo("Save Product")
        }

        // Assert load product is not called
        coVerify(exactly = 0) { loadProduct(any()) }
    }

    @Test
    fun `onEvent OnDeleteProduct, should emit NavigateOnDelete side effect`() = runTest {
        // Arrange
        val itemSku = "SKU001"
        savedStateHandle["itemSku"] = itemSku

        // The viewModel must be initialized first.
        viewModel = AddEditProductViewModel(
            saveProduct,
            loadProduct,
            deleteProduct,
            resourceProvider,
            savedStateHandle
        )

        // Act
        viewModel.onEvent(AddEditProductEvent.OnDeleteProduct)

        // Assert side effect
        viewModel.sideEffectFlow.test {
            assertThat(awaitItem()).isEqualTo(AddEditProductSideEffect.NavigateOnDelete)
            cancelAndIgnoreRemainingEvents()
        }

        // Verify use case is called
        coVerify (exactly = 1){ deleteProduct(itemSku) }
    }

    @Test
    fun `onEvent onSaveProduct, should emit NavigateOnSave side effect`() = runTest {
        // Arrange
        val itemSku = "SKU001"
        savedStateHandle["itemSku"] = itemSku

        // The viewModel must be initialized first.
        viewModel = AddEditProductViewModel(
            saveProduct,
            loadProduct,
            deleteProduct,
            resourceProvider,
            savedStateHandle
        )

        // Act
        viewModel.onEvent(AddEditProductEvent.OnSaveProduct)

        // Assert side effect
        viewModel.sideEffectFlow.test {
            assertThat(awaitItem()).isEqualTo(AddEditProductSideEffect.NavigateOnSave)
            cancelAndIgnoreRemainingEvents()
        }

        // Verify use case is called
        coVerify (exactly = 1){ saveProduct(any()) }
    }

    @Test
    fun `onEvent OnNameChange, should update itemName in uiState`() = runTest {
        // Arrange
        val itemSku = "SKU001"
        savedStateHandle["itemSku"] = itemSku

        // The viewModel must be initialized first.
        viewModel = AddEditProductViewModel(
            saveProduct,
            loadProduct,
            deleteProduct,
            resourceProvider,
            savedStateHandle
        )

        // Act
        viewModel.onEvent(AddEditProductEvent.OnNameChange("New Name"))

        // Asser ui state
        viewModel.uiState.test {
            val finalState = awaitItem()
            assertThat(finalState.itemName).isEqualTo("New Name")
        }
    }

    @Test
    fun `onEvent OnVariationChange, should update itemVariant in uiState`() = runTest {
        // Arrange
        val itemSku = "SKU001"
        savedStateHandle["itemSku"] = itemSku

        // The viewModel must be initialized first.
        viewModel = AddEditProductViewModel(
            saveProduct,
            loadProduct,
            deleteProduct,
            resourceProvider,
            savedStateHandle
        )

        // Act
        viewModel.onEvent(AddEditProductEvent.OnVariationChange("New Variation"))

        // Asser ui state
        viewModel.uiState.test {
            val finalState = awaitItem()
            assertThat(finalState.itemVariant).isEqualTo("New Variation")
        }
    }

    @Test
    fun `onEvent OnPhotoSelected, should update uri in uiState`() = runTest {
        // Arrange
        val mockUri = mockk<Uri>()
        val itemSku = "SKU001"
        savedStateHandle["itemSku"] = itemSku

        // The viewModel must be initialized first.
        viewModel = AddEditProductViewModel(
            saveProduct,
            loadProduct,
            deleteProduct,
            resourceProvider,
            savedStateHandle
        )

        // Act
        viewModel.onEvent(AddEditProductEvent.OnPhotoSelected(mockUri))

        // Asser ui state
        viewModel.uiState.test {
            val finalState = awaitItem()
            assertThat(finalState.itemPhotoUri).isEqualTo(mockUri)
            cancelAndIgnoreRemainingEvents()
        }
    }


}

//    @Test
//    fun `onDeleteProduct when sku is not null, should show loading, call delete use case, and navigate on success`() =
//        runTest {
// Arrange
//val itemSku = "test-sku"
//savedStateHandle["itemSku"] = "test-sku"

// Arrange
//            viewModel = AddEditProductViewModel(
//                saveProduct,
//                loadProduct,
//                deleteProduct,
//                resourceProvider,
//                savedStateHandle
//            )

// Act & Assert using turbineScope
//            turbineScope {
// 1. Launch a turbine for each flow you want to test
//                val uiStateTurbine = viewModel.uiState.testIn(this)
//                val sideEffectTurbine = viewModel.sideEffectFlow.testIn(this)
//
//                // --- Assertions for uiStateTurbine ---
//
//                // The ViewModel's init block might cause an initial state emission.
//                // Consume the initial state to start with a clean slate.
//                // In your case, init() loads a product, so it goes loading -> not loading.
//                assertThat(uiStateTurbine.awaitItem().isLoading).isFalse() // Initial state from constructor
//                assertThat(uiStateTurbine.awaitItem().isLoading).isTrue()  // From onLoadProduct()
//                assertThat(uiStateTurbine.awaitItem().isLoading).isFalse() // From onLoadProduct() finally block
//
//                // --- Trigger the event ---
//                viewModel.onEvent(AddEditProductEvent.OnDeleteProduct)
//
//                // --- Assert state changes and side effects ---
//
//                // Assert StateFlow (uiState) changes
//                assertThat(uiStateTurbine.awaitItem().isLoading).isTrue()   // Start of onDeleteProduct
//                assertThat(uiStateTurbine.awaitItem().isLoading).isFalse()  // finally block in onDeleteProduct
//
//                // Assert SharedFlow (sideEffectFlow) emission
//                assertThat(sideEffectTurbine.awaitItem()).isEqualTo(AddEditProductSideEffect.NavigateOnDelete)
//
//                // --- Final verifications ---
//
//                // Verify that the correct use case was called
//                coVerify(exactly = 1) { deleteProduct(itemSku) }
//
//                // Ensure no other events were emitted to the flows
//                sideEffectTurbine.expectNoEvents()
//                uiStateTurbine.cancelAndIgnoreRemainingEvents()
//            }
//
//        }
