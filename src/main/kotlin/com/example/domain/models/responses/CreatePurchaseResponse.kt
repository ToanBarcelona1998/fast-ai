package com.example.domain.models.responses

import com.example.domain.models.entity.Purchase

import kotlinx.serialization.*

@Serializable
data class CreatePurchaseResponse(private val purchase: Purchase)