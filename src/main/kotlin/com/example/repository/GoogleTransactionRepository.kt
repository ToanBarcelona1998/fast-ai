package com.example.repository

import com.example.data.GoogleClient
import com.example.repository.interfaces.ITransactionRepository

class GoogleTransactionRepository(private val googleClient: GoogleClient) : ITransactionRepository {
    override suspend fun verifyTransaction(verifyData: String): Boolean {
        return googleClient.verifyGooglePlayPurchase()
    }
}