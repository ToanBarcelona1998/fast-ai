package com.example.repository

import com.example.database.UserCreditTable
import com.example.domain.models.entity.UserCredit
import com.example.domain.models.requests.UserCreditUpdateRequest
import com.example.repository.interfaces.IUserCreditRepository
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

final class UserCreditRepository : IUserCreditRepository {
    override suspend fun getCreditByUserId(id: Int): UserCredit? {
        return transaction {
            UserCreditTable.selectAll().where(UserCreditTable.userId eq id).limit(1).map {
                UserCredit(id = it[UserCreditTable.id], remaining = it[UserCreditTable.remainingCredits])
            }.firstOrNull()
        }
    }

    override suspend fun update(id: Int, request: UserCreditUpdateRequest): Boolean {
        return transaction {
            val now = Clock.System.now()
            UserCreditTable.update({ UserCreditTable.userId eq id }) {
                it.update(remainingCredits , remainingCredits + request.creditChange)
                it[updatedAt] = now
            }

            true
        }
    }
}