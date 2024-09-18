package com.example.repository

import com.example.repository.interfaces.IAuthRepository

class AuthRepository : IAuthRepository{
    override fun login(email: String, password: String): Void {
        TODO("Not yet implemented")
    }

    override fun socialLogin() {
        TODO("Not yet implemented")
    }
}