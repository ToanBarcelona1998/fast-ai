package com.example.services

import com.example.domain.models.User
import com.example.repository.interfaces.IUserRepository

class UserService(private val userRepository: IUserRepository) {
    fun getAllUsers(page: Int, limit : Int) : Int {
        return 1
    }

    fun addUser() {

    }

    fun getUser(id : Int) : User? {
//        val user = userRepository.get<User?>(id)
//
//        return user

        return null
    }
}