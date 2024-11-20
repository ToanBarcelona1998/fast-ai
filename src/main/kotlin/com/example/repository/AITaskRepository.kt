package com.example.repository

import com.example.database.AITaskTable
import com.example.domain.models.entity.AITask
import com.example.domain.models.entity.BasePaging
import com.example.domain.models.requests.AITaskAddRequest
import com.example.domain.models.requests.Paging
import com.example.repository.interfaces.IAITaskRepository
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

final class AITaskRepository : IAITaskRepository {
    override suspend fun getHistoryTasks(userId: Int, paging: Paging): BasePaging<List<AITask>> {
        return transaction {
            val validPage = if (paging.offset <= 0) 1 else paging.offset

            val totalUserAITasks = AITaskTable.select(AITaskTable.userId eq userId).count()

            val totalPages = (totalUserAITasks / paging.limit) + if (totalUserAITasks % paging.limit > 0) 1 else 0

            // Check if the requested page is valid based on total AITasks

            val calculateLimit = (validPage - 1) * paging.offset
            if (calculateLimit >= totalUserAITasks) {
                // If the requested page exceeds total AITasks, return an empty list
                return@transaction BasePaging(
                    data = emptyList(),
                    currentPage = validPage,
                    pageSize = paging.limit,
                    totalPages = totalPages
                )
            }

            val tasks = AITaskTable.selectAll().limit(paging.limit, calculateLimit.toLong())
                .where(AITaskTable.userId eq userId).map {
                    AITask(
                        id = it[AITaskTable.id],
                        userId = it[AITaskTable.userId],
                        createdAt = it[AITaskTable.createdAt].toString(),
                        data = it[AITaskTable.data],
                        taskType = it[AITaskTable.taskType],
                        rawData = it[AITaskTable.rawData]
                    )
                }

            BasePaging(
                data = tasks,
                currentPage = validPage,
                pageSize = paging.limit,
                totalPages = totalPages
            )
        }
    }

    override suspend fun add(request: AITaskAddRequest): AITask {
        return transaction {
            val now = Clock.System.now()

            val id = AITaskTable.insert {
                it[createdAt] = now
                it[userId] = request.userId
                it[rawData] = request.rawData
                it[taskType] = request.taskType
                it[data] = request.data
            }[AITaskTable.id]

            AITask(
                id = id,
                userId = request.userId,
                taskType = request.taskType,
                createdAt = now.toString(),
                rawData = request.rawData,
                data = request.data
            )
        }
    }
}