package com.example.application.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database


object DatabaseConfig {
    private const val JDBC_URL = "jdbc:postgresql://localhost:5432/fastai"
    private const val DRIVER_CLASS_NAME = "org.postgresql.Driver"

    fun initDatabase(){
        val config = HikariConfig().apply {
            jdbcUrl = JDBC_URL
            username = "toannv"
            password = ""
            driverClassName = DRIVER_CLASS_NAME
        }

        val dataSource = HikariDataSource(config)

        Database.connect(dataSource)
    }
}