package com.example.repository

import com.example.database.AccountTable
import com.example.database.UserTable
import com.example.domain.models.responds.User
import com.example.repository.interfaces.IUserRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class UserRepository : IUserRepository {
    override suspend fun add(entity: User): User {
        return transaction {
            val id = UserTable.insert {
                it[userName] = entity.userName
                it[gender] = entity.gender
                it[isActive] = entity.isActive
                it[accountId] = entity.accountId
                it[avatar] = entity.avatar
                it[address] = entity.address
                it[createdDate] = entity.createAt
                it[updatedDate] = entity.updateAt
                it[birthday] = entity.birthday
                it[phoneNumber] = entity.phoneNumber
            }
            entity.copy(id = id.insertedCount)
        }
    }

    override suspend fun delete(id: Int): Boolean {
        return transaction {
            UserTable.update({ UserTable.id eq id }){
                it[isActive] = false
            }

            true
        }
    }

    override suspend fun update(id: Int, entity: User): Boolean {
        return transaction {
            UserTable.update ({ UserTable.id eq id }){
                it[userName] = entity.userName
                it[gender] = entity.gender
                it[isActive] = entity.isActive
                it[accountId] = entity.accountId
                it[avatar] = entity.avatar
                it[address] = entity.address
                it[createdDate] = entity.createAt
                it[updatedDate] = entity.updateAt
                it[birthday] = entity.birthday
                it[phoneNumber] = entity.phoneNumber
            }

            true
        }
    }

    override suspend fun get(id: Int): User? {
        return transaction {
            val user = AccountTable.innerJoin(UserTable).select((UserTable.accountId eq AccountTable.id) and (UserTable.id eq id)).map {
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
                    it[UserTable.updatedDate],
                    it[UserTable.createdDate],
                    it[UserTable.isActive],
                )
            }.firstOrNull()

            user
        }
    }

}