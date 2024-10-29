package com.example.domain.models.entity

data class Purchase(val id: Int, val userId: Int, val methodId: Int, val packageId: Int, val creditsPurchased: Int , val createdAt : String , val status: String)
