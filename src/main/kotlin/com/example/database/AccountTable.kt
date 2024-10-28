package com.example.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object AccountTable : Table("accounts") {
    val id = integer("id").autoIncrement()
    val email = varchar("email",255).uniqueIndex()
    val password = varchar("password" , 255)
    val status = integer("status").default(0)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at").nullable()

    override val primaryKey = PrimaryKey(id)
}