package com.example.repository.interfaces

interface IAuthRepository {
    fun login(email: String , password:  String) : Void

    fun socialLogin()
}