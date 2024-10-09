package com.example.application.plugins

import com.example.application.di.injection
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configKoin(){
    install(Koin){
        slf4jLogger()
        modules(injection)
    }
}