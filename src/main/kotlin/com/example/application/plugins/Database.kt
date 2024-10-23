package com.example.application.plugins

import com.example.application.config.DatabaseConfig
import io.ktor.server.application.*

fun Application.configDb(){
    DatabaseConfig.initDatabase()
}