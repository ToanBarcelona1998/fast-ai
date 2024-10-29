package com.example.domain.models.requests

data class PurchaseAddRequest(val userId: Int, val methodId: Int , val packageId : Int , val creditsPurchased : Int)
