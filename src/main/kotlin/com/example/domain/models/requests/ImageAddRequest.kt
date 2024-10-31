package com.example.domain.models.requests

data class ImageAddRequest(
    val userId: Int,
    val s3Url: String,
    val width: Int?,
    val height: Int?,
    val fileFormat: String,
)