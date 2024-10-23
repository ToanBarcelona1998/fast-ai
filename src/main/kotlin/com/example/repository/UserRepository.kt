package com.example.repository

import com.example.database.AccountTable
import com.example.database.UserTable
import com.example.domain.models.requests.UserAddRequest
import com.example.domain.models.requests.UserUpdateRequest
import com.example.domain.models.responds.User
import com.example.repository.interfaces.IUserRepository
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class UserRepository : IUserRepository {
    override suspend fun add(request: UserAddRequest): User {
        return transaction {
            val id = UserTable.insert {
                it[userName] = request.userName
                it[gender] = request.gender
                it[isActive] = true
                it[accountId] = request.accountId
                it[avatar] = request.avatar
                it[address] = request.address
                it[birthday] = request.birthday
                it[phoneNumber] = request.phoneNumber
            }

            val createdAt = Clock.System.now().toString()

            User(
                id = id.insertedCount,
                userName = request.userName,
                gender = request.gender,
                isActive = true,
                updateAt = null,
                email = request.email,
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
                it[isActive] = false
            }

            true
        }
    }

    override suspend fun update(id: Int, request: UserUpdateRequest): User {
        return transaction {

            val user = AccountTable.innerJoin(UserTable)
                .select((UserTable.accountId eq AccountTable.id) and (UserTable.id eq id)).map {
                    User(
                        it[UserTable.id],
                        it[UserTable.userName],
                        it[AccountTable.email],
                        it[UserTable.phoneNumber],
                        it[UserTable.gender],
                        it[UserTable.address],
                        it[UserTable.avatar],
                        it[UserTable.birthday],
                        it[UserTable.accountId],
                        it[UserTable.updatedDate]?.toString(),
                        it[UserTable.createdDate].toString(),
                        it[UserTable.isActive],
                    )
                }.firstOrNull()

            UserTable.update({ UserTable.id eq id }) {
                if(!request.userName.isNullOrEmpty()){
                    it[userName] = request.userName
                }

                if(request.gender != null){
                    it[gender] = request.gender
                }

                if(request.isActive != null){
                    it[isActive] = request.isActive
                }

                it[avatar] = request.avatar
                it[avatar] = request.avatar
                it[address] = request.address
                it[birthday] = request.birthday
                it[phoneNumber] = request.phoneNumber
            }

            user!!.copyWith()
        }
    }

    override suspend fun get(id: Int): User? {
        return transaction {
            val user = AccountTable.innerJoin(UserTable)
                .select((UserTable.accountId eq AccountTable.id) and (UserTable.id eq id)).map {
                    User(
                        it[UserTable.id],
                        it[UserTable.userName],
                        it[AccountTable.email],
                        it[UserTable.phoneNumber],
                        it[UserTable.gender],
                        it[UserTable.address],
                        it[UserTable.avatar],
                        it[UserTable.birthday],
                        it[UserTable.accountId],
                        it[UserTable.updatedDate]?.toString(),
                        it[UserTable.createdDate].toString(),
                        it[UserTable.isActive],
                    )
                }.firstOrNull()

            user
        }
    }

}