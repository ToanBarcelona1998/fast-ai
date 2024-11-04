package com.example.client

import io.ethers.core.types.Hash
import io.ethers.providers.Provider

class Web3Client(private val web3: Provider) {
    suspend fun verifyTxHash(tx: String): Boolean {

        val txReceipt = web3.getTransactionReceipt(hash = Hash(tx)).sendAwait().unwrapOrNull()?.get()

        return txReceipt != null && txReceipt.isSuccessful
    }
}