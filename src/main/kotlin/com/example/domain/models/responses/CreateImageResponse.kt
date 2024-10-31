package com.example.domain.models.responses

import com.example.domain.models.entity.Image
import kotlinx.serialization.*

@Serializable
data class CreateImageResponse(val image: Image)