package com.example.services

import com.example.domain.exceptions.FastAiException
import com.example.domain.models.requests.AITaskAddRequest
import com.example.domain.models.requests.Paging
import com.example.domain.models.responses.CreateAITaskResponse
import com.example.domain.models.responses.GetHistoryTasksResponse
import com.example.domain.models.responses.PagingResponse
import com.example.repository.interfaces.IAITaskRepository
import com.example.utils.catchBlockService

class AITaskService(private val aiTaskResponse: IAITaskRepository) {
    suspend fun add(userId: Int?, data: String?, rawData: String?, taskType: String?): CreateAITaskResponse {
        return catchBlockService {

            if (userId == null) {
                throw FastAiException(
                    FastAiException.AI_TASK_MISSING_USER_ID_ERROR_CODE,
                    FastAiException.AI_TASK_MISSING_USER_ID_ERROR_MESSAGE
                )
            }

            if (taskType.isNullOrEmpty()) {
                throw FastAiException(
                    FastAiException.AI_TASK_MISSING_TASK_TYPE_ERROR_CODE,
                    FastAiException.AI_TASK_MISSING_TASK_TYPE_ERROR_MESSAGE
                )
            }

            if (data.isNullOrEmpty()) {
                throw FastAiException(
                    FastAiException.AI_TASK_MISSING_DATA_ERROR_CODE,
                    FastAiException.AI_TASK_MISSING_DATA_ERROR_MESSAGE
                )
            }

            val request = AITaskAddRequest(userId = userId, data = data, rawData = rawData, taskType = taskType)

            val task = aiTaskResponse.add(request)

            CreateAITaskResponse(task)
        }
    }

    suspend fun getHistoryImages(userId: Int?, page: Int?, limit: Int?): GetHistoryTasksResponse {
        return catchBlockService {
            if (userId == null) {
                throw FastAiException(
                    FastAiException.AI_TASK_MISSING_USER_ID_ERROR_CODE,
                    FastAiException.AI_TASK_MISSING_USER_ID_ERROR_MESSAGE
                )
            }

            val paging = Paging(offset = page ?: 1, limit = limit ?: Int.MAX_VALUE)

            val task = aiTaskResponse.getHistoryTasks(userId = userId, paging = paging)

            GetHistoryTasksResponse(
                tasks = task.data,
                PagingResponse(currentPage = task.currentPage, pageSize = task.pageSize, totalPages = task.totalPages)
            )
        }
    }
}