package com.example.domain.models.responses
import kotlinx.serialization.*

@Serializable
data class UploadMultiFileResponse(val urls: List<String>)