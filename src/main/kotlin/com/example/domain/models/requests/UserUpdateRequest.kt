package com.example.domain.models.requests

data class UserUpdateRequest(
    val userName: String?,
    val phoneNumber : String?,
    val gender : Int?,
    val address: String?,
    val birthday : String?,
    val avatar : String?,
    val isActive : Boolean?
)
