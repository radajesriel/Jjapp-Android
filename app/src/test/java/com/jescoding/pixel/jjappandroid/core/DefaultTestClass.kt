package com.jescoding.pixel.jjappandroid.core

import com.jescoding.pixel.jjappandroid.core.data.providers.FakeResourceProviderImpl
import com.jescoding.pixel.jjappandroid.core.data.providers.FakeResourceProviderImpl.Companion.ADD_HEADER_ID
import com.jescoding.pixel.jjappandroid.core.data.providers.FakeResourceProviderImpl.Companion.EDIT_HEADER_ID
import com.jescoding.pixel.jjappandroid.core.data.providers.FakeResourceProviderImpl.Companion.SAVE_BUTTON_ID
import com.jescoding.pixel.jjappandroid.core.data.providers.FakeResourceProviderImpl.Companion.UPDATE_BUTTON_ID
import com.jescoding.pixel.jjappandroid.core.domain.providers.DispatcherProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before

@ExperimentalCoroutinesApi
abstract class DefaultTestClass {

    @MockK
    internal lateinit var dispatcherProvider: DispatcherProvider
    internal val testDispatcher = UnconfinedTestDispatcher()
    internal val resourceProvider = FakeResourceProviderImpl()


    @Before
    open fun setUp() {
        every { dispatcherProvider.io } returns testDispatcher
        every { dispatcherProvider.main } returns testDispatcher
        every { dispatcherProvider.default } returns testDispatcher
    }

}