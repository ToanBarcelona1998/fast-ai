package com.example.database

import org.jetbrains.exposed.sql.Table

object AccountTable : Table("Account") {
    val id = integer("id").autoIncrement()
    val email = varchar("email",255).uniqueIndex()
    val password = varchar("pass_word" , 255)
    val createdAt = varchar("created_at", 255)
    val updateAt = varchar("update_at", 255).nullable()

    override val primaryKey = PrimaryKey(UserTable.id)
}