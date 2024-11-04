package com.example.repository.interfaces

import com.example.domain.models.entity.AITask
import com.example.domain.models.entity.BasePaging
import com.example.domain.models.requests.AITaskAddRequest
import com.example.domain.models.requests.Paging

interface IAITaskRepository : IAddRepository<AITask,AITaskAddRequest>{
    suspend fun getHistoryTasks(userId: Int, paging: Paging) : BasePaging<List<AITask>>
}