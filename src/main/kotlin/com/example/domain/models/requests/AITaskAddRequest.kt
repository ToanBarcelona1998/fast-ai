package com.example.domain.models.requests

data class AITaskAddRequest(
    val userId: Int,
    val data: String,
    val rawData : String?,
    val taskType : String
)