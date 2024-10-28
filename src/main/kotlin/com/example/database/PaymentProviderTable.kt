package com.example.database

import org.jetbrains.exposed.sql.Table

object PaymentProviderTable : Table("payment_providers") {
    val id = integer("id").autoIncrement()
    val name = varchar("name" , 50)
    val description = varchar("description" , 255).nullable()

    override val primaryKey = PrimaryKey(UserCreditTable.id)
}