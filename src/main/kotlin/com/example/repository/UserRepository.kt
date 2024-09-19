package com.example.repository

import com.example.database.UserTable
import com.example.repository.interfaces.IUserRepository
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository : IUserRepository {
    override fun <T> add(param: T) {
        TODO("Not yet implemented")
    }

    override fun <T> delete(id: T) {
        TODO("Not yet implemented")
    }

    override fun <T> update(param: T) {
        TODO("Not yet implemented")
    }

    override fun <T> get(id: Int): T {
//        return transaction {
//            UserTable.select(UserTable.id eq id)
//        }
        TODO("Not yet implemented")
    }
}