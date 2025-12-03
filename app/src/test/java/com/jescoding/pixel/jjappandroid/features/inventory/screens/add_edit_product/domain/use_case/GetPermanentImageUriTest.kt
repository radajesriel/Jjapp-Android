package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.use_case

import android.net.Uri
import androidx.compose.ui.text.input.KeyboardType
import com.google.common.truth.Truth.assertThat
import com.jescoding.pixel.jjappandroid.core.DefaultTestClass
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.repository.ProductImageRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class GetPermanentImageUriTest : DefaultTestClass() {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var productImageRepository: ProductImageRepository
    private lateinit var getPermanentImageUri: GetPermanentImageUri

    @Before
    override fun setUp() {
        super.setUp()
        getPermanentImageUri = GetPermanentImageUri(
            repository = productImageRepository,
            dispatcherProvider = dispatcherProvider
        )
    }

    @Test
    // Should use mockk to create mock Uri instances since this is from Android SDK
    fun `invoke() with valid temporary uri should return permanent uri from repository`() =
        runTest {
            // Arrange
            val tempUri = mockk<Uri>() // A mock Uri representing the temporary image
            val permanentUri = mockk<Uri>() // A mock Uri representing the returned permanent image

            // Mock the repository to return the permanent Uri when called with the temp one
            coEvery { productImageRepository.copyImageToInternalStorage(tempUri) } returns permanentUri

            // Act
            val result = getPermanentImageUri(tempUri)

            // Assert
            assertThat(result).isNotNull()
            assertThat(result).isEqualTo(permanentUri)
        }

    @Test
    // Should use mockk to create mock Uri instances since this is from Android SDK
    fun `invoke() when repository fails to copy should return null`() = runTest {
        // Arrange
        val tempUri = mockk<Uri>()

        // Mock the repository to return null (e.g., copy failed, file was invalid)
        coEvery { productImageRepository.copyImageToInternalStorage(tempUri) } returns null

        // Act
        val result = getPermanentImageUri(tempUri)

        // Assert
        assertThat(result).isNull()
    }

    @Test
    // Should use mockk to create mock Uri instances since this is from Android SDK
    fun `invoke() when repository throws exception should propagate exception`() = runTest {
        // Arrange
        val tempUri = mockk<Uri>()
        val testException = IOException("Permission denied to write to internal storage")

        // Mock the repository to throw an exception
        coEvery { productImageRepository.copyImageToInternalStorage(tempUri) } throws testException

        // Act & Assert
        try {
            getPermanentImageUri(tempUri)
            // If this line is reached, the test fails because no exception was thrown.
            throw AssertionError("Expected an IOException to be thrown")
        } catch (e: IOException) {
            // Success: The correct exception type was caught.
            assertThat(e).isInstanceOf(IOException::class.java)
            assertThat(e.message).isEqualTo("Permission denied to write to internal storage")
        }
    }

}