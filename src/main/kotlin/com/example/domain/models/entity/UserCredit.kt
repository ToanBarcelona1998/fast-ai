package com.example.domain.models.entity

import kotlinx.serialization.*

@Serializable
data class UserCredit(val id : Int,val remaining : Int)