package com.example.application.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database


object DatabaseConfig {
    private const val DRIVER_CLASS_NAME = "org.postgresql.Driver"

    fun initDatabase(){
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://${ApplicationConfig.getDatabaseUrl()}/${ApplicationConfig.getDatabaseName()}"
            username = ApplicationConfig.getDatabaseUserName()
            password = ""
            driverClassName = DRIVER_CLASS_NAME
        }

        val dataSource = HikariDataSource(config)

        Database.connect(dataSource)
    }
}