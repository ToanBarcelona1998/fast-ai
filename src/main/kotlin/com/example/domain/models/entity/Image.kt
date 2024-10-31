package com.example.domain.models.entity

import kotlinx.serialization.*

@Serializable
data class Image(
    val id: Int,
    val userId: Int,
    val s3Url: String,
    val width: Int?,
    val height: Int?,
    val fileFormat: String,
    val createdAt: String,
)