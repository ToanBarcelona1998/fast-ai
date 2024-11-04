package com.example.domain.models.entity

import kotlinx.serialization.*

enum class PaymentType(val type : String){
    Web3("web3"),
    Google("google"),
    Apple("apple");

    companion object{
        fun findByType(type : String) : PaymentType? {
            return PaymentType.entries.find { it.type == type }
        }
    }
}

@Serializable
data class PaymentProvider(val id: Int, val name : String , val type : String, val description : String?)