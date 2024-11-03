package com.example.repository

import com.example.database.ModelTable
import com.example.domain.models.entity.Model
import com.example.domain.models.requests.ModelAddRequest
import com.example.repository.interfaces.IModelRepository
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

final class ModelRepository : IModelRepository {
    override suspend fun add(request: ModelAddRequest): Model {
        return transaction {
            val now = Clock.System.now()
            val id = ModelTable.insert {
                it[model] = request.model
                it[tags] = request.tags
                it[detail] = request.detail
                it[type] = request.type
                it[thumbnail] = request.thumbnail
                it[createdAt] = now
            }[ModelTable.id]

            Model(
                id = id,
                model = request.model,
                tags = request.tags,
                type = request.type,
                thumbnail = request.thumbnail,
                detail = request.detail,
                createdAt = now.toString(),
                updatedAt = null
            )
        }
    }

    override suspend fun getAll(): List<Model> {
        return transaction {
            ModelTable.selectAll().map {
                Model(
                    id = it[ModelTable.id],
                    model = it[ModelTable.model],
                    tags = it[ModelTable.tags],
                    type = it[ModelTable.type],
                    thumbnail = it[ModelTable.thumbnail],
                    detail = it[ModelTable.detail],
                    createdAt = it[ModelTable.createdAt].toString(),
                    updatedAt = it[ModelTable.updatedAt]?.toString()
                )
            }
        }
    }

    override suspend fun getAll(request: Int): List<Model> {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: Int): Model? {
        return transaction {
            ModelTable.selectAll().where(ModelTable.id eq id).limit(1).map {
                Model(
                    id = it[ModelTable.id],
                    model = it[ModelTable.model],
                    tags = it[ModelTable.tags],
                    type = it[ModelTable.type],
                    thumbnail = it[ModelTable.thumbnail],
                    detail = it[ModelTable.detail],
                    createdAt = it[ModelTable.createdAt].toString(),
                    updatedAt = it[ModelTable.updatedAt]?.toString()
                )
            }.firstOrNull()
        }
    }
}