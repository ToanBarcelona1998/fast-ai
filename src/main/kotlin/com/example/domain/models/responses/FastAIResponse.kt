package com.example.domain.models.responses

import com.example.domain.models.entity.AITask

import kotlinx.serialization.*

@Serializable
data class FastAIResponse (val tasks : List<AITask>)