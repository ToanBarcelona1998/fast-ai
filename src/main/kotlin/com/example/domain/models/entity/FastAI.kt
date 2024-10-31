package com.example.domain.models.entity

import kotlinx.serialization.*

@Serializable
data class FastAIImage(
    val taskType: String,
    val imageUUID: String,
    val imageURL: String,
    val NSFWContent: Boolean?
)

@Serializable
data class FastAIControlNetImage(
    val taskUUID: String,
    val inputImageUUID: String,
    val guideImageURL: String,
)

@Serializable
data class FastAIEnhancedPrompt(
    val taskUUID: String,
    val text: String,
)

@Serializable
data class FastAIImageToText(
    val taskType: String,
    val taskUUID: String,
    val text: String,
)

data class FastAIData<T>(val data : List<T>)