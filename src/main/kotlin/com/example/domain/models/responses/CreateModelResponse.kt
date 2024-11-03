package com.example.domain.models.responses

import com.example.domain.models.entity.Model
import kotlinx.serialization.*

@Serializable
data class CreateModelResponse(val model : Model)