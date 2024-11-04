package com.example.services

import com.example.domain.exceptions.FastAiException
import com.example.domain.models.requests.AITaskAddRequest
import com.example.domain.models.requests.Paging
import com.example.domain.models.responses.CreateImageResponse
import com.example.domain.models.responses.GetHistoryImagesResponse
import com.example.repository.interfaces.IAITaskRepository
import com.example.utils.catchBlockService

class AITaskService(private val imageRepository: IAITaskRepository) {
    suspend fun add(userId : Int? , data: String? , rawData : String? , taskType : String?): CreateImageResponse {
        return catchBlockService {

            if(userId == null){
                throw FastAiException(FastAiException.AI_TASK_MISSING_USER_ID_ERROR_CODE, FastAiException.AI_TASK_MISSING_USER_ID_ERROR_MESSAGE)
            }

            if(taskType.isNullOrEmpty()){
                throw FastAiException(FastAiException.AI_TASK_MISSING_TASK_TYPE_ERROR_CODE,FastAiException.AI_TASK_MISSING_TASK_TYPE_ERROR_MESSAGE)
            }

            if(data.isNullOrEmpty()){
                throw FastAiException(FastAiException.AI_TASK_MISSING_DATA_ERROR_CODE,FastAiException.AI_TASK_MISSING_DATA_ERROR_MESSAGE)
            }

            val request = AITaskAddRequest(userId=  userId , data = data ,rawData = rawData , taskType = taskType)

            val image = imageRepository.add(request)

            CreateImageResponse(image)
        }
    }

    suspend fun getHistoryImages(userId : Int?, page : Int? , limit : Int?) : GetHistoryImagesResponse {
        return catchBlockService {
            if(userId == null){
                throw FastAiException(FastAiException.AI_TASK_MISSING_USER_ID_ERROR_CODE, FastAiException.AI_TASK_MISSING_USER_ID_ERROR_MESSAGE)
            }

            val paging = Paging(offset = page ?: 1 , limit = limit ?: Int.MAX_VALUE)

            val images = imageRepository.getHistoryTasks(userId = userId, paging = paging)

            GetHistoryImagesResponse(images)
        }
    }
}