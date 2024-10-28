package com.example.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object UserCreditTable : Table("user_credits") {
    val id = integer("id").autoIncrement()
    val userId = reference("user_id" , UserTable.id)
    val remainingCredits = integer("remaining_credits").default(0)
    val updatedAt = timestamp("updated_at").nullable()

    override val primaryKey = PrimaryKey(id)
}