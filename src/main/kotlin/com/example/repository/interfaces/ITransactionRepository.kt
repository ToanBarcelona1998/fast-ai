package com.example.repository.interfaces

interface ITransactionRepository {
    suspend fun verifyTransaction(verifyData : String) : Boolean
}