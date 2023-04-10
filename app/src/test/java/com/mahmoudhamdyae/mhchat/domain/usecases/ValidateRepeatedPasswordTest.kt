package com.mahmoudhamdyae.mhchat.domain.usecases

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ValidateRepeatedPasswordTest {

    private lateinit var validateRepeatedPassword: ValidateRepeatedPassword

    @Before
    fun setUp() {
        validateRepeatedPassword = ValidateRepeatedPassword()
    }

    @Test
    fun repeatedPasswordIsNotTheSame_ReturnsError() {
        val result = validateRepeatedPassword("Abcdefg1", "Abcdefg2")

        Assert.assertEquals(result.successful, false)
    }

    @Test
    fun repeatedPasswordIsOk_ReturnsNoError() {
        val result = validateRepeatedPassword("Abcdefg1", "Abcdefg1")

        Assert.assertEquals(result.successful, true)
    }
}