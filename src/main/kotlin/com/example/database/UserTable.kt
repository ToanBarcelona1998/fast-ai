package com.example.database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object UserTable : Table("users") {
    val id  = integer("id").autoIncrement()
    val userName = varchar("user_name",100)
    val phoneNumber = varchar("phone_number", 15).nullable()
    val gender = integer("gender")
    val address = varchar("address" , length = 255).nullable()
    val birthday = varchar("birthday" , length = 100).nullable()
    val avatar = varchar("avatar" , length = 255).nullable()
    val accountId = reference("account_id", AccountTable.id)
    val createdAt = timestamp("created_at")
    val status = integer("status").default(0) // 0 : waiting update. 1 : active. 2 : locked. 3 : deleted
    val updatedAt = timestamp("updated_at").nullable()

    override val primaryKey = PrimaryKey(id)
}