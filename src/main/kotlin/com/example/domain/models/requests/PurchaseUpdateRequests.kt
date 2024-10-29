package com.example.domain.models.requests
import kotlinx.serialization.*

@Serializable
data class PurchaseUpdateRequests(val id: Int,val status: String, val data : String?)