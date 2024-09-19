package com.example.database

import org.jetbrains.exposed.sql.ForeignKeyConstraint
import org.jetbrains.exposed.sql.Table

object UserTable : Table("user") {
    val id  = integer("id").autoIncrement()
    val userName = varchar("user_name",255)
    val phoneNumber = varchar("phone_number", 255).nullable()
    val gender = integer("gender")
    val address = varchar("address" , length = 255).nullable()
    val birthday = varchar("birthday" , length = 255).nullable()
    val avatar = varchar("avatar" , length = 255).nullable()
    val accountId = reference("account_id", AccountTable.id)
    val createdDate = varchar("created_date", length = 255)
    val isActive = bool("is_active")
    val updatedDate = varchar("updated_date", length = 255).nullable()

    override val primaryKey = PrimaryKey(id)
}