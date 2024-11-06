package com.example.services

import com.example.application.config.JWTConfig
import com.example.domain.exceptions.FastAiException
import com.example.domain.models.responses.LoginResponse
import com.example.domain.models.responses.RegisterResponse
import com.example.utils.catchBlockService
import com.example.utils.verifyPassword

final class AuthService(private val accountService: AccountService ,private val userService: UserService) {
    suspend fun register(email : String?, password: String?) : RegisterResponse{
        return catchBlockService {
            val account = accountService.createAccount(email,password)

            val user = userService.createUser(email, accountId = account.id, gender = 0, address = null, birthday = null, phoneNumber = null, avatar = null, status = 0)

            val accessToken = JWTConfig.makeJWTToken(userId = user.id)

            RegisterResponse(accessToken= accessToken)
        }
    }

    suspend fun login(email: String?, password: String?) : LoginResponse{
        return catchBlockService {
            val account = accountService.getAccountByEmail(email)

            if(password.isNullOrEmpty()){
                throw FastAiException(FastAiException.MISSING_PASSWORD_ERROR_CODE , message = FastAiException.MISSING_PASSWORD_ERROR_MESSAGE)
            }

            if (!verifyPassword(password,account.password)){
                throw FastAiException(FastAiException.INCORRECT_PASSWORD_ERROR_CODE , message = FastAiException.INCORRECT_PASSWORD_ERROR_MESSAGE)
            }

            val user = userService.getUserByAccountId(account.id)

            val accessToken = JWTConfig.makeOnBoardingJWTToken(user.id)

            LoginResponse(accessToken)
        }
    }
}