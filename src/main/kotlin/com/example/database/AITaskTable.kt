package com.example.database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object AITaskTable : Table("ai_tasks") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(UserTable.id)
    val taskType = varchar("task_type", 50)
    val data = varchar("data" , 255)
    val rawData = varchar("raw_data",255).nullable()
    val createdAt = timestamp("created_at")

    override val primaryKey = PrimaryKey(id)
}