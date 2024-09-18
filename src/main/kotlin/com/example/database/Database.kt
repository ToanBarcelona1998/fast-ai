package com.example.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

fun initDatabase(){
    val config = HikariConfig().apply {
        jdbcUrl = ""
        username = ""
        password = ""
        driverClassName = ""
    }

    val dataSource = HikariDataSource(config)

    Database.connect(dataSource)
}