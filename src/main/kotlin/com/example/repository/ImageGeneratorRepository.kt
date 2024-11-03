package com.example.repository

import com.example.database.ImageTable
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

            val totalUserImages = ImageTable.select(ImageTable.userId eq userId).count()

            // Check if the requested page is valid based on total images

            val calculateLimit = (validPage - 1) * paging.offset
            if ( calculateLimit >= totalUserImages) {
                // If the requested page exceeds total images, return an empty list
                return@transaction emptyList()
            }

            ImageTable.selectAll().limit(paging.limit, calculateLimit.toLong())
                .where(ImageTable.userId eq userId).map {
                Image(
                    id = it[ImageTable.id],
                    userId = it[ImageTable.userId],
                    height = it[ImageTable.height],
                    s3Url = it[ImageTable.s3Url],
                    createdAt = it[ImageTable.createdAt].toString(),
                    fileFormat = it[ImageTable.fileFormat],
                    width = it[ImageTable.width]
                )
            }
        }
    }

    override suspend fun add(request: ImageAddRequest): Image {
        return transaction {
            val now = Clock.System.now()

            val id = ImageTable.insert {
                it[createdAt] = now
                it[userId] = request.userId
                it[s3Url] = request.s3Url
                it[height] = request.height
                it[width] = request.width
                it[fileFormat] = request.fileFormat
            }[ImageTable.id]

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