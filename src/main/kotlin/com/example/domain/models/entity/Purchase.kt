package com.example.domain.models.entity

import kotlinx.serialization.*

enum class PurchaseStatus(val status : String){
    Completed("completed"),
    Failed("failed"),
    Pending("Pending");

    companion object{
        fun findByStatus(status : String) : PurchaseStatus? {
            return PurchaseStatus.entries.find { it.status == status }
        }

        fun isAccessed(status: String) : Boolean{
            return findByStatus(status) != null
        }
    }
}

@Serializable
data class Purchase(val id: Int, val userId: Int, val methodId: Int, val packageId: Int, val creditsPurchased: Int , val createdAt : String , val status: String,val updatedAt : String?){
    fun isCompleted() : Boolean{
        return status == "completed" || status == "failed"
    }
}
