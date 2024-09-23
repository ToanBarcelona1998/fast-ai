package com.example.domain.models.responds

data class User(
    val id: Int?,
    val userName: String,
    val email: String,
    val phoneNumber : String?,
    val gender : Int,
    val address: String?,
    val birthday : String?,
    val avatar : String?,
    val accountId : Int,
    val updateAt : String?,
    val createAt : String,
    val isActive : Boolean
)
