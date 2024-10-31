package com.example.repository

import com.example.client.Web3Client
import com.example.repository.interfaces.ITransactionRepository

class Web3TransactionRepository(private val web3Client: Web3Client) : ITransactionRepository {
    override suspend fun verifyTransaction(verifyData: String): Boolean {
        return web3Client.verifyTxHash(verifyData)
    }
}