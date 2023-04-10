package com.mahmoudhamdyae.mhchat.domain.usecases

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ValidateEmailTest {

    private lateinit var validateEmail: ValidateEmail

    @Before
    fun setUp() {
        validateEmail = ValidateEmail()
    }

    @Test
    fun emailIsBlank_ReturnsError() {
        val result = validateEmail("")

        Assert.assertEquals(result.successful, false)
    }
}