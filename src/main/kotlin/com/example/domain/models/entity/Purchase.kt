package com.example.domain.models.entity

import kotlinx.serialization.*

@Serializable
data class Purchase(val id: Int, val userId: Int, val methodId: Int, val packageId: Int, val creditsPurchased: Int , val createdAt : String , val status: String,val updatedAt : String?){
    fun isCompleted() : Boolean{
        return status == "completed" || status == "failed"
    }
}
