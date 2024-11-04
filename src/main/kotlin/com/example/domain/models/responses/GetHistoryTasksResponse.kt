package com.example.domain.models.responses

import com.example.domain.models.entity.AITask
import kotlinx.serialization.*

@Serializable
class GetHistoryTasksResponse(val tasks : List<AITask> , val paging : PagingResponse)