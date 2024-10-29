package com.example.domain.models.responses

import com.example.domain.models.entity.UserCredit

import kotlinx.serialization.*

@Serializable
data class GetUserCreditResponse(private val userCredit: UserCredit)
