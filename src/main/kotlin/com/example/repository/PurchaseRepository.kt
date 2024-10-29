package com.example.repository

import com.example.database.PurchaseTable
import com.example.domain.models.entity.Purchase
import com.example.domain.models.requests.PurchaseAddRequest
import com.example.repository.interfaces.IPurchaseRepository
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

final class PurchaseRepository : IPurchaseRepository {
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
                createdAt = now.toString()
            )
        }
    }
}