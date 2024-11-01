package com.example.domain.models.entity

import kotlinx.serialization.*

@Serializable
data class PaymentProvider(val id: Int, val name : String , val type : String, val description : String?)