package com.example.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object PurchaseTable : Table("purchases") {
    val id = integer("id").autoIncrement()
    val userId = reference("user_id",UserTable.id)
    val status = varchar("status", 50).default("pending") // pending , completed , failed
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("created_at").nullable()
    val paymentProviderId = reference("payment_provider_id",PaymentProviderTable.id)
    val creditsPurchased = integer("credits_purchased").default(0)
    val packageId = reference("package_id" , PackageTable.id)
    val data = varchar("data", 255).nullable()

    override val primaryKey = PrimaryKey(UserCreditTable.id)
}