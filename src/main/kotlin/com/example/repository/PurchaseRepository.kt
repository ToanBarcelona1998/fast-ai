package com.example.repository


import com.example.database.PurchaseTable
import com.example.domain.models.entity.Purchase
import com.example.domain.models.requests.PurchaseAddRequest
import com.example.domain.models.requests.PurchaseUpdateRequest
import com.example.domain.models.requests.PurchaseUpdateRequests
import com.example.repository.interfaces.IPurchaseRepository
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

final class PurchaseRepository : IPurchaseRepository {
    override suspend fun multiUpdate(requests: List<PurchaseUpdateRequests>): Boolean {
        return transaction {
            val now = Clock.System.now()

            for (request in requests) {
                PurchaseTable.update({ PurchaseTable.id eq request.id }) {
                    it[status] = request.status
                    it[data] = request.data
                    it[updatedAt] = now
                }
            }
            true
        }
    }

    override suspend fun add(request: PurchaseAddRequest): Purchase {
        return transaction {
            val now = Clock.System.now()

            val createdStatus = "pending"

            val id = PurchaseTable.insert {
                it[userId] = request.userId
                it[paymentProviderId] = request.methodId
                it[status] = createdStatus
                it[createdAt] = now
                it[packageId] = request.packageId
                it[creditsPurchased] = request.creditsPurchased
            }[PurchaseTable.id]

            Purchase(
                id = id,
                userId = request.userId,
                status = createdStatus,
                packageId = request.packageId,
                creditsPurchased = request.creditsPurchased,
                methodId = request.methodId,
                createdAt = now.toString(),
                updatedAt = null
            )
        }
    }

    override suspend fun update(id: Int, request: PurchaseUpdateRequest): Boolean {
        return transaction {
            val now = Clock.System.now()

            PurchaseTable.update({ PurchaseTable.id eq id }) {
                it[status] = request.status
                it[data] = request.data
                it[updatedAt] = now
            }

            true
        }
    }

    override suspend fun get(id: Int): Purchase? {
        return transaction {
            PurchaseTable.selectAll().where(PurchaseTable.id eq id).map {
                Purchase(
                    id = it[PurchaseTable.id],
                    packageId = it[PurchaseTable.packageId],
                    status = it[PurchaseTable.status],
                    methodId = it[PurchaseTable.paymentProviderId],
                    userId = it[PurchaseTable.userId],
                    createdAt = it[PurchaseTable.createdAt].toString(),
                    creditsPurchased = it[PurchaseTable.creditsPurchased],
                    updatedAt = it[PurchaseTable.updatedAt]?.toString()
                )
            }.firstOrNull()
        }
    }
}