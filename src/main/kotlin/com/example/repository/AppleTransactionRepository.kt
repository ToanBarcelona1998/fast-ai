package com.example.repository

import com.example.data.AppleClient
import com.example.repository.interfaces.ITransactionRepository

class AppleTransactionRepository(private val appleClient: AppleClient) : ITransactionRepository {
    override suspend fun verifyTransaction(verifyData: String): Boolean {
        return appleClient.verifyApplePurchase(verifyData)
    }
}