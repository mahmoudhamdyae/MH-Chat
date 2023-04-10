package com.mahmoudhamdyae.mhchat.domain.usecases

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ValidateUserNameTest {

    private lateinit var validateUserName: ValidateUserName

    @Before
    fun setUp() {
        validateUserName = ValidateUserName()
    }

    @Test
    fun userNameIsBlank_ReturnsError() {
        val result = validateUserName("")

        Assert.assertEquals(result.successful, false)
    }

    @Test
    fun userNameIsOk_ReturnsNoError() {
        val result = validateUserName("A")

        Assert.assertEquals(result.successful, true)
    }
}