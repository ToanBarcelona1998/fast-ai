package com.example.utils

fun validateEmail(email : String) : Boolean{
    val emailRegex = Regex(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )
    return emailRegex.matches(email)
}

fun validatePurchaseStatus(status: String) : Boolean{
    val allowStatus = listOf("pending","completed" , "failed")

    return allowStatus.contains(status)
}