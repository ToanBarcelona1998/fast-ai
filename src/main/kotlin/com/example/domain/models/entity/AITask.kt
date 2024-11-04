package com.example.domain.models.entity

import kotlinx.serialization.*

@Serializable
data class AITask(
    val id: Int,
    val userId: Int,
    val data: String,
    val taskType: String,
    val rawData: String?,
    val createdAt: String,
)