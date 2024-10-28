package com.example.domain.models.requests

import java.math.BigDecimal

data class PackageAddRequest(
    val name: String,
    val credits: Int,
    val description: String?,
    val basePrice: BigDecimal,
    val promoPrice: BigDecimal?,
    val promoEndTime: String?,
    val updatedAt: String?,
)