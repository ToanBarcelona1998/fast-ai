package com.example.domain.models.responses

import com.example.domain.models.entity.AITask
import kotlinx.serialization.*

@Serializable
data class CreateAITaskResponse(val task: AITask)