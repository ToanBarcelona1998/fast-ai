package com.example.database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object ImageTable : Table("images") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(UserTable.id)
    val s3Url = text("s3_url")
    val width = integer("width").nullable()
    val height = integer("height").nullable()
    val fileFormat = varchar("file_format", 10).default("png")
    val createdAt = timestamp("created_at")

    override val primaryKey = PrimaryKey(id)
}