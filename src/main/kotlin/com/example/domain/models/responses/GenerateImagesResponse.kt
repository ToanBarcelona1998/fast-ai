package com.example.domain.models.responses

import com.example.domain.models.entity.Image

import kotlinx.serialization.*

@Serializable
data class GenerateImagesResponse(val images: List<Image>)