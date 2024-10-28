package com.example.domain.models.entity

import kotlinx.serialization.*
import java.math.BigDecimal

@Serializable
data class Package(
    val id: Int,
    val name: String,
    val credits: Int,
    val description: String?,
    val isActive: Boolean,
    @Contextual val basePrice: BigDecimal,
    @Contextual val promoPrice: BigDecimal?,
    val promoEndTime : String?,
    val createdAt : String,
    val updatedAt : String?,
)