package com.example.infrastructure

import com.example.domain.exceptions.RunWareExceptions
import com.example.domain.models.entity.*
import com.example.domain.models.requests.*
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
                setBody(listOf(request))
                headers {
                    append("Authorization" , "Bearer $fastApiKey")
                    append("Accept" , "application/json")
                }
            }

            if(response.status != HttpStatusCode.OK){
                val errorResponse = response.body<RunWareExceptions>()
                throw errorResponse.toBaseException()
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

    suspend fun enhancePrompt(request: EnhancePromptTaskRequest) : FastAIData<FastAIEnhancedPrompt>{
        return call<FastAIData<FastAIEnhancedPrompt>,EnhancePromptTaskRequest>(request)
    }

    suspend fun controlNetProcessor(request: ControlNetPreprocessTaskRequest) : FastAIData<FastAIControlNetImage>{
        return call<FastAIData<FastAIControlNetImage>, ControlNetPreprocessTaskRequest>(request)
    }
}