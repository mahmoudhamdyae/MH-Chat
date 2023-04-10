package com.mahmoudhamdyae.mhchat.domain.usecases

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ValidatePasswordTest {

    private lateinit var validatePassword: ValidatePassword

    @Before
    fun setUp() {
        validatePassword = ValidatePassword()
    }

    @Test
    fun passwordIsLetterOnly_ReturnsError() {
        val result = validatePassword("Abcdefgh")

        assertEquals(result.successful, false)
    }

    @Test
    fun passwordIsBlank_ReturnsError() {
        val result = validatePassword("")

        assertEquals(result.successful, false)
    }

    @Test
    fun passwordLengthIsLessThanSix_ReturnsError() {
        val result = validatePassword("Abcd1")

        assertEquals(result.successful, false)
    }

    @Test
    fun passwordIsLowerCaseOnly_ReturnsError() {
        val result = validatePassword("abcdefg1")

        assertEquals(result.successful, false)
    }

    @Test
    fun passwordIsUpperCaseOnly_ReturnsError() {
        val result = validatePassword("ABCDEFG1")

        assertEquals(result.successful, false)
    }

    @Test
    fun passwordIsOk_ReturnsNoError() {
        val result = validatePassword("Abcdefg1")

        assertEquals(result.successful, true)
    }
}