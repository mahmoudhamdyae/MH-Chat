package com.mahmoudhamdyae.mhchat.domain.services

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}