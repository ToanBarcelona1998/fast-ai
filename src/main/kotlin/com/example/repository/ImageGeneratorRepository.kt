package com.example.repository

import com.example.database.ImagesTable
import com.example.domain.models.entity.Image
import com.example.domain.models.requests.ImageAddRequest
import com.example.domain.models.requests.Paging
import com.example.repository.interfaces.IImageGeneratorRepository
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

final class ImageGeneratorRepository : IImageGeneratorRepository {
    override suspend fun getHistoryImages(userId: Int, paging: Paging): List<Image> {
        return transaction {
            val validPage = if (paging.offset <= 0) 1 else paging.offset

            val totalUserImages = ImagesTable.select(ImagesTable.userId eq userId).count()

            // Check if the requested page is valid based on total images

            val calculateLimit = (validPage - 1) * paging.offset
            if ( calculateLimit >= totalUserImages) {
                // If the requested page exceeds total images, return an empty list
                return@transaction emptyList()
            }

            ImagesTable.selectAll().limit(paging.limit, calculateLimit.toLong())
                .where(ImagesTable.userId eq userId).map {
                Image(
                    id = it[ImagesTable.id],
                    userId = it[ImagesTable.userId],
                    height = it[ImagesTable.height],
                    s3Url = it[ImagesTable.s3Url],
                    createdAt = it[ImagesTable.createdAt].toString(),
                    fileFormat = it[ImagesTable.fileFormat],
                    width = it[ImagesTable.width]
                )
            }
        }
    }

    override suspend fun add(request: ImageAddRequest): Image {
        return transaction {
            val now = Clock.System.now()

            val id = ImagesTable.insert {
                it[createdAt] = now
                it[userId] = request.userId
                it[s3Url] = request.s3Url
                it[height] = request.height
                it[width] = request.width
                it[fileFormat] = request.fileFormat
            }[ImagesTable.id]

            Image(
                id = id,
                userId = request.userId,
                height = request.height,
                s3Url = request.s3Url,
                createdAt = now.toString(),
                fileFormat = request.fileFormat,
                width = request.width
            )
        }
    }
}