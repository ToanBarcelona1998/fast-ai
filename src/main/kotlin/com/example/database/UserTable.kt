package com.example.database

import org.jetbrains.exposed.sql.Table

object UserTable : Table("User") {
    val id  = integer("id").autoIncrement()
    val userName = varchar("user_name",255)
    val email = varchar("email" , 255)

    override val primaryKey = PrimaryKey(id)
}