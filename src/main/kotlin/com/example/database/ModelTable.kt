package com.example.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object ModelTable : Table("ai_models"){
    val id = integer("id").autoIncrement()
    val model = varchar("model",50)
    val type = varchar("model_type", 50)
    val tags = varchar("tags" , 500)
    val thumbnail = varchar("thumbnail", 100)
    val detail = varchar("model_detail", 100).nullable()
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at").nullable()
    val defaultNegativePrompt = varchar("default_negative_prompt" , 2048).nullable()

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
