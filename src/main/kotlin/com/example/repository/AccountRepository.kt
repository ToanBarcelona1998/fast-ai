package com.example.repository

import com.example.database.AccountTable
import com.example.database.UserTable
import com.example.domain.models.requests.AccountAddRequest
import com.example.domain.models.requests.AccountUpdateRequest
import com.example.domain.models.entity.Account
import com.example.repository.interfaces.IAccountRepository
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

final class AccountRepository : IAccountRepository {
    override suspend fun existsByEmail(email: String): Boolean {
        return transaction {
            val row = AccountTable.select(AccountTable.email eq email).limit(1).firstOrNull()

            row != null
        }
    }

    override suspend fun add(request: AccountAddRequest): Account {
        return transaction {

            val createAt = Clock.System.now()

            val id = AccountTable.insert {
                it[email] = request.email
                it[password] = request.password
                it[createdAt] = createAt
            } get AccountTable.id

            Account(id = id, request.email, request.password , createAt.toString() , null)
        }
    }

    override suspend fun delete(id: Int): Boolean {
        return transaction {
            true
        }
    }

    override suspend fun update(id: Int, request: AccountUpdateRequest): Account {
        return transaction {

            val updateAt = Clock.System.now()

            val account = AccountTable.select(AccountTable.id eq id).limit(1).map {
                Account(
                    it[AccountTable.id],
                    it[AccountTable.email],
                    it[AccountTable.password],
                    it[AccountTable.createdAt].toString(),
                    it[AccountTable.updatedAt]?.toString(),
                )
            }.firstOrNull()

            AccountTable.update({ AccountTable.id eq id }) {
                if (!request.email.isNullOrEmpty()) {
                    it[email] = request.email
                }

                if (!request.password.isNullOrEmpty()) {
                    it[password] = request.password
                }

                it[updatedAt] = updateAt
            }

            account!!.copyWith(
                email = request.email,
                password = request.password,
                updateAt = updateAt.toString(),
            )
        }
    }

    override suspend fun get(id: Int): Account? {
        return transaction {
            AccountTable.select(AccountTable.id eq id).limit(1).map {
                Account(
                    it[AccountTable.id],
                    it[AccountTable.email],
                    it[AccountTable.password],
                    it[AccountTable.createdAt].toString(),
                    it[AccountTable.updatedAt]?.toString(),
                )
            }.firstOrNull()
        }
    }

}