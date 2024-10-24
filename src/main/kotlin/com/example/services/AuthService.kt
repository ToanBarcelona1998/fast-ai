package com.example.services

import com.example.application.config.JWTConfig
import com.example.domain.exceptions.FastAiException
import com.example.domain.models.responses.LoginResponse
import com.example.domain.models.responses.RegisterResponse

final class AuthService(private val accountService: AccountService ,private val userService: UserService) {
    suspend fun register(email : String?, password: String?) : RegisterResponse{
        return try{
            val account = accountService.createAccount(email,password)

            val user = userService.createUser(email, accountId = account.id, gender = 0, address = null, birthday = null, phoneNumber = null, avatar = null)

            val accessToken = JWTConfig.makeJWTToken(userId = user.id)

            RegisterResponse(accessToken= accessToken)
        }catch (e: FastAiException){
            throw e
        }
    }

    suspend fun login(email: String?, password: String?) : LoginResponse{
        return try{
            val id = accountService.getAccountIDByEmailAndPassword(email,password)

            val user = userService.getUserById(id)

            val accessToken = JWTConfig.makeJWTToken(user.id)

            LoginResponse(accessToken)
        }catch (e: FastAiException){
            throw e
        }
    }
}