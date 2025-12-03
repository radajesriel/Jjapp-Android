package com.jescoding.pixel.jjappandroid.core.utils

import org.junit.Assert
import org.junit.Test

class ValidationUtilsTest {

    @Test
    fun `getValidatedCurrency with empty string returns empty string`() {
        Assert.assertEquals("", ValidationUtils.getValidatedCurrency(""))
    }

    @Test
    fun `getValidatedCurrency with integer string returns same string`() {
        Assert.assertEquals("123", ValidationUtils.getValidatedCurrency("123"))
    }

    @Test
    fun `getValidatedCurrency with valid decimal string returns same string`() {
        Assert.assertEquals("123.45", ValidationUtils.getValidatedCurrency("123.45"))
    }

    @Test
    fun `getValidatedCurrency with more than two decimal places truncates`() {
        Assert.assertEquals("123.45", ValidationUtils.getValidatedCurrency("123.456"))
    }

    @Test
    fun `getValidatedCurrency with multiple decimal points keeps only the first`() {
        Assert.assertEquals("123.45", ValidationUtils.getValidatedCurrency("123.4.5"))
    }

    @Test
    fun `getValidatedCurrency with leading decimal point is handled`() {
        Assert.assertEquals(".45", ValidationUtils.getValidatedCurrency(".45"))
    }

    @Test
    fun `getIntegerOnly with decimals and symbols returns only digits`() {
        Assert.assertEquals("987", ValidationUtils.getIntegerOnly("98.7%"))
    }

    @Test
    fun `getIntegerOnly with no digits returns empty string`() {
        Assert.assertEquals("", ValidationUtils.getIntegerOnly("abc.xyz-%"))
    }

}