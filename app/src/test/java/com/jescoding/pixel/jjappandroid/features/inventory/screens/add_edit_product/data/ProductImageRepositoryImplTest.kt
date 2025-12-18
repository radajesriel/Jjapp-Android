package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.data

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.input.KeyboardType
import com.google.common.base.Verify.verify
import com.jescoding.pixel.jjappandroid.core.DefaultTestClass
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.repository.ProductImageRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@ExperimentalCoroutinesApi
class ProductImageRepositoryImplTest : DefaultTestClass() {

    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    private lateinit var context: Context
    @MockK
    private lateinit var contentResolver: ContentResolver
    private lateinit var productImageRepository: ProductImageRepository
    private lateinit var tempCacheDir: File

    @Before
    override fun setUp() {
        super.setUp()
        // Create a temporary directory before each test
        tempCacheDir = File.createTempFile("test_cache", "").apply {
            delete() // Delete the file to create a directory
            mkdir()
        }
        every { context.contentResolver } returns contentResolver
        every { context.cacheDir } returns tempCacheDir // Use the real temp directory

        productImageRepository = ProductImageRepositoryImpl(
            context = context,
            dispatcherProvider = dispatcherProvider
        )
    }

    @After
    fun tearDown() {
        tempCacheDir.deleteRecursively()
    }

    @Test
    fun `copyImageToInternalStorage should return new Uri when stream is copied successfully`() =
        runTest {
              // Given
            val fakeContent = "fake image data"
            val fakeInputStream = ByteArrayInputStream(fakeContent.toByteArray())
            val mockImageUri = mockk<Uri>()
            val mockResultUri = mockk<Uri>()

            every { contentResolver.openInputStream(mockImageUri) } returns fakeInputStream

            // Mock the static Uri.fromFile to control the output
            mockkStatic(Uri::class)
            every { Uri.fromFile(any()) } returns mockResultUri

            // When
            val result = productImageRepository.copyImageToInternalStorage(mockImageUri)

            // Then
            Assert.assertNotNull("Resulting Uri should not be null", result)
            Assert.assertEquals(
                "Result should be the Uri returned by Uri.fromFile",
                mockResultUri, result
            )

            // Verify the file was created and has the correct content
            val filesInCache = tempCacheDir.listFiles()
            Assert.assertTrue(
                "Cache directory should not be empty",
                filesInCache?.isNotEmpty() == true
            )
            val copiedFile = filesInCache?.first()
            Assert.assertNotNull(
                "A file should have been created in the cache directory",
                copiedFile
            )
            Assert.assertEquals(
                "File content should match the input stream",
                fakeContent, copiedFile?.readText()
            )

            // Verify the input stream was closed (implicitly by copyTo)
            // No need to verify copyTo directly, as the file content check confirms it worked.
        }


}