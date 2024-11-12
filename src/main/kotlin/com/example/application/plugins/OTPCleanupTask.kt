package com.example.application.plugins

import com.example.utils.OTP_EXPIRATION_TIME
import com.example.utils.otpStorage
import io.ktor.server.application.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
fun Application.cleanUpOtpTask(){
    GlobalScope.launch {
        while (true){
            delay(60 * 60 * 1000)

            val currentTime = System.currentTimeMillis()

            otpStorage.entries.removeIf { (_, value) ->
                currentTime - value.second > OTP_EXPIRATION_TIME
            }
        }
    }
}