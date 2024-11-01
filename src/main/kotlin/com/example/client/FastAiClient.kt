package com.example.client

import com.example.domain.exceptions.RunWareExceptions
import com.example.domain.models.entity.FastAIData
import com.example.domain.models.entity.FastAIImage
import com.example.domain.models.entity.FastAIImageToText
import com.example.domain.models.requests.ImageTaskRequest
import com.example.domain.models.requests.ImageToTextTaskRequest
import com.example.domain.models.requests.RemoveImageBackgroundTaskRequest
import com.example.domain.models.requests.UpScaleGanTaskRequest
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*

class FastAiClient(private val httpClient: HttpClient,private val fastAIUrl: String , private val fastApiKey : String) {
    private suspend inline fun <reified T, reified R> call(request : R) : T{
        try {
            val response = httpClient.post(fastAIUrl){
                contentType(ContentType.Application.Json)
                setBody(request)
                headers {
                    append("Authorization" , "Bearer $fastApiKey")
                    append("Accept" , "application/json")
                }
            }

            return response.body<T>()
        }catch (e: ResponseException){
            val errorResponse = e.response.body<RunWareExceptions>()

            throw errorResponse.toBaseException()
        }
    }


    suspend fun generateImages(request : ImageTaskRequest) : FastAIData<FastAIImage> {
        return call<FastAIData<FastAIImage>,ImageTaskRequest>(request)
    }

    suspend fun removeBackground(request: RemoveImageBackgroundTaskRequest): FastAIData<FastAIImage>{
        return call<FastAIData<FastAIImage>,RemoveImageBackgroundTaskRequest>(request)
    }

    suspend fun upScaleImage(request : UpScaleGanTaskRequest) : FastAIData<FastAIImage>{
        return call<FastAIData<FastAIImage>,UpScaleGanTaskRequest>(request)
    }

    suspend fun imageToText(request : ImageToTextTaskRequest) : FastAIData<FastAIImageToText>{
        return call<FastAIData<FastAIImageToText>,ImageToTextTaskRequest>(request)
    }
}