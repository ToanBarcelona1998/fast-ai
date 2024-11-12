package com.example.utils

import java.util.concurrent.ConcurrentHashMap

// Store OTPs with timestamp
val otpStorage = ConcurrentHashMap<String, Pair<String, Long>>()

const val OTP_EXPIRATION_TIME = 15 * 60 * 1000

fun generateOTP() : String{
    return (100000..999999).random().toString()
}