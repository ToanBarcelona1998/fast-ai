package com.example.client

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class AppleClient(val httpClient: HttpClient, val itunesUrl : String , secretKey : String) {
    suspend fun verifyApplePurchase(token : String) : Boolean{
        return try{
            val response =  httpClient.post(itunesUrl){
                contentType(ContentType.Application.Json)

            }

            if(response.status != HttpStatusCode.OK){
                false
            }

            true
        }catch (e : Exception){
            throw e
        }
    }
}