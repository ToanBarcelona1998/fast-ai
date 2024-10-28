package com.example.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object PackageTable : Table("packages") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val credits = integer("credits")
    val description = varchar("description",255).nullable()
    val isActive = bool("is_active").default(true)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at").nullable()
    val basePrice = decimal("base_price", 10 ,2 )
    val promoPrice = decimal("promo_price" , 10, 2).nullable()
    val promoEndTime = timestamp("promo_end_time").nullable()
}