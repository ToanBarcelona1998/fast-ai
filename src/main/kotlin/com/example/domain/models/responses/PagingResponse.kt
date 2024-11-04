package com.example.domain.models.responses

import kotlinx.serialization.*

@Serializable
data class PagingResponse(val currentPage: Int, val pageSize: Int, val totalPages: Long)