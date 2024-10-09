package com.example.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object AccountTable : Table("accounts") {
    val id = integer("id").autoIncrement()
    val email = varchar("email",255).uniqueIndex()
    val password = varchar("password" , 255)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at").nullable()

    override val primaryKey = PrimaryKey(id)
}