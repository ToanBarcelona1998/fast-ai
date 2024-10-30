package com.example.repository

import com.example.database.UserTable
import com.example.domain.models.requests.UserAddRequest
import com.example.domain.models.requests.UserUpdateRequest
import com.example.domain.models.entity.User
import com.example.repository.interfaces.IUserRepository
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class UserRepository : IUserRepository {
    override suspend fun getUserByAccountID(id: Int): User? {
        return transaction {
            val user = UserTable.selectAll().where { UserTable.accountId eq id }.limit(1).map {
                User(
                    it[UserTable.id],
                    it[UserTable.userName],
                    it[UserTable.phoneNumber],
                    it[UserTable.gender],
                    it[UserTable.address],
                    it[UserTable.avatar],
                    it[UserTable.birthday],
                    it[UserTable.accountId],
                    it[UserTable.updatedAt]?.toString(),
                    it[UserTable.createdAt].toString(),
                    it[UserTable.status],
                )
            }.firstOrNull()

            user
        }
    }

    override suspend fun add(request: UserAddRequest): User {
        return transaction {
            val id = UserTable.insert {
                it[userName] = request.userName
                it[gender] = request.gender
                it[status] = request.status
                it[accountId] = request.accountId
                it[avatar] = request.avatar
                it[address] = request.address
                it[birthday] = request.birthday
                it[phoneNumber] = request.phoneNumber
            } get UserTable.id

            val createdAt = Clock.System.now().toString()

            User(
                id = id,
                userName = request.userName,
                gender = request.gender,
                status = request.status,
                updateAt = null,
                createAt = createdAt,
                phoneNumber = request.phoneNumber,
                accountId = request.accountId,
                birthday = request.birthday,
                avatar = request.avatar,
                address = request.address,
            )
        }
    }

    override suspend fun delete(id: Int): Boolean {
        return transaction {
            UserTable.update({ UserTable.id eq id }) {
                it[status] = 3
            }

            true
        }
    }

    override suspend fun update(id: Int, request: UserUpdateRequest): Boolean {
        return transaction {
            val updateAt = Clock.System.now()

            UserTable.update({ UserTable.id eq id }) {
                if (!request.userName.isNullOrEmpty()) {
                    it[userName] = request.userName
                }

                if (request.gender != null) {
                    it[gender] = request.gender
                }

                if (request.status != null) {
                    it[status] = request.status
                }

                if(request.address != null){
                    it[address] = request.address
                }

                if(request.birthday != null){
                    it[birthday] = request.birthday
                }

                if(request.avatar != null){
                    it[avatar] = request.avatar
                }

                if(request.phoneNumber != null){
                    it[phoneNumber] = request.phoneNumber
                }

                it[updatedAt] = updateAt
            }

            true
        }
    }

    override suspend fun get(id: Int): User? {
        return transaction {
            val user = UserTable.selectAll().where { UserTable.id eq id }.limit(1).map {
                User(
                    it[UserTable.id],
                    it[UserTable.userName],
                    it[UserTable.phoneNumber],
                    it[UserTable.gender],
                    it[UserTable.address],
                    it[UserTable.avatar],
                    it[UserTable.birthday],
                    it[UserTable.accountId],
                    it[UserTable.updatedAt]?.toString(),
                    it[UserTable.createdAt].toString(),
                    it[UserTable.status],
                )
            }.firstOrNull()

            user
        }
    }

}