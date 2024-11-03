package com.example.domain.models.entity
import kotlinx.serialization.*

@Serializable
data class Model(
    val id: Int,
    val model: String,
    val type: String,
    val tags: String,
    val thumbnail: String,
    val detail: String?,
    val createdAt: String,
    val updatedAt: String?
)