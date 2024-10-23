package com.example.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object AccountTable : Table("account") {
    val id = integer("id").autoIncrement()
    val email = varchar("email",255).uniqueIndex()
    val password = varchar("password" , 255)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at").nullable()

    override val primaryKey = PrimaryKey(id)
}