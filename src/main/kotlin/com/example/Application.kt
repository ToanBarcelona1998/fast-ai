package com.example

import com.example.application.di.httpClient
import com.example.application.plugins.configureHTTP
import com.example.application.plugins.configureMonitoring
import com.example.application.plugins.configureRouting
import com.example.application.plugins.configureSecurity
import com.example.application.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)

    Runtime.getRuntime().addShutdownHook(Thread{
        httpClient.close()
    })
}

fun Application.module() {
    configureHTTP()
    cleanUpOtpTask()
    configDb()
    configContentNegotiation()
    configureMonitoring()
    configKoin()
    configureSecurity()
    configureRouting()
}
