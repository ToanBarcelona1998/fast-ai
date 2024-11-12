package com.example.services

import com.example.application.config.ApplicationConfig
import com.example.application.config.JWTConfig
import com.example.domain.exceptions.FastAiException
import com.example.domain.models.responses.LoginResponse
import com.example.domain.models.responses.RegisterResponse
import com.example.infrastructure.EmailClient
import com.example.utils.catchBlockService
import com.example.utils.generateOTP
import com.example.utils.verifyPassword

final class AuthService(private val accountService: AccountService ,private val userService: UserService, private val emailClient: EmailClient) {
    suspend fun register(email : String?, password: String?) : RegisterResponse{
        return catchBlockService {
            val account = accountService.createAccount(email,password)

            val user = userService.createUser(email, accountId = account.id, gender = 0, address = null, birthday = null, phoneNumber = null, avatar = null, status = 0)

            sendVerifyCode(email)

            val accessToken = JWTConfig.makeOnBoardingJWTToken(userId = user.id)

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

            val accessToken = JWTConfig.makeJWTToken(user.id)

            LoginResponse(accessToken)
        }
    }

    private suspend fun sendVerifyCode(email: String?) : Boolean{
        return catchBlockService {

            if(email.isNullOrEmpty()){
                throw FastAiException(FastAiException.MISSING_EMAIL_ERROR_CODE, FastAiException.MISSING_EMAIL_ERROR_MESSAGE)
            }

            val otp = generateOTP()

            val smtpEmail = ApplicationConfig.getSmtpEmail()

            emailClient.sendEmail(to = email , from = smtpEmail , subject = "Fast AI verification" , content = "Your otp code: $otp")
        }
    }
}