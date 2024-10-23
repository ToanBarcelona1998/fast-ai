package com.example.domain.models.requests

data class UserAddRequest(
    val userName: String,
    val email: String,
    val phoneNumber: String?,
    val gender: Int,
    val address: String?,
    val birthday: String?,
    val avatar: String?,
    val accountId: Int,
)