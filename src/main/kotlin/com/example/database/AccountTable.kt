package com.example.database

import org.jetbrains.exposed.sql.Table

object AccountTable : Table("account") {
    val id = integer("id").autoIncrement()
    val email = varchar("email",255).uniqueIndex()
    val password = varchar("password" , 255)
    val createdAt = varchar("created_at", 255)
    val updatedAt = varchar("updated_at", 255).nullable()

    override val primaryKey = PrimaryKey(id)
}