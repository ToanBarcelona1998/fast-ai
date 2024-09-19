package com.example

import com.example.application.plugins.configureHTTP
import com.example.application.plugins.configureMonitoring
import com.example.application.plugins.configureRouting
import com.example.application.plugins.configureSecurity
import com.example.application.plugins.*
import com.example.repository.UserRepository
import com.example.services.UserService
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureHTTP()
    configureMonitoring()
    configureSecurity(UserService(UserRepository()))
    configureRouting()
}
