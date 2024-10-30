package com.example.repository

import com.example.database.PaymentProviderTable
import com.example.domain.models.entity.PaymentProvider
import com.example.domain.models.requests.PaymentProviderAddRequest
import com.example.repository.interfaces.IPaymentProviderRepository
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

final class PaymentProviderRepository : IPaymentProviderRepository {
    override suspend fun getMethodByName(name: String): PaymentProvider? {
        return transaction {
            PaymentProviderTable.selectAll().where(PaymentProviderTable.name eq name).limit(1).map {
                PaymentProvider(
                    id = it[PaymentProviderTable.id],
                    name = it[PaymentProviderTable.name],
                    description = it[PaymentProviderTable.description],
                    type = it[PaymentProviderTable.type]
                )
            }.firstOrNull()
        }
    }

    override suspend fun add(request: PaymentProviderAddRequest): PaymentProvider {
        return transaction {
            val id = PaymentProviderTable.insert {
                it[name] = request.name
                it[description] = request.description
                it[type] = request.type
            }[PaymentProviderTable.id]

            PaymentProvider(id = id, name = request.name, description = request.description , type = request.type)
        }
    }

    override suspend fun getAll(): List<PaymentProvider> {
        return transaction {
            PaymentProviderTable.selectAll().orderBy(PaymentProviderTable.name, SortOrder.DESC).map {
                PaymentProvider(
                    id = it[PaymentProviderTable.id],
                    name = it[PaymentProviderTable.name],
                    description = it[PaymentProviderTable.description],
                    type = it[PaymentProviderTable.type]
                )
            }
        }
    }

    override suspend fun getAll(request: Int): List<PaymentProvider> {
        TODO("Not yet implemented")
    }
}