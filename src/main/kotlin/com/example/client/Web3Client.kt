package com.example.client

import java.net.http.HttpClient

class Web3Client(private val client: HttpClient,private val web3Url : String) {
    suspend fun verifyTxHash(tx : String) : Boolean{
        return true
    }
}