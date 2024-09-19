package com.example.database

import org.jetbrains.exposed.sql.Table

object UserTable : Table("User") {
    val id  = integer("id").autoIncrement()
    val userName = varchar("user_name",255)
    val email = varchar("email" , 255)
    val avatar = varchar("avatar" , length = 255)
    val createdDate = varchar("created_date", length = 255)
    val isActive = bool("is_active")
    val gender = integer("gender")
    val updatedDate = varchar("updated_date", length = 255)

    override val primaryKey = PrimaryKey(id)
}