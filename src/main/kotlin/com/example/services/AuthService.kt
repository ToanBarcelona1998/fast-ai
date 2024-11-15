package com.example.services

import com.example.application.config.ApplicationConfig
import com.example.application.config.JWTConfig
import com.example.domain.exceptions.FastAiException
import com.example.domain.models.responses.LoginResponse
import com.example.domain.models.responses.RefreshResponse
import com.example.domain.models.responses.RegisterResponse
import com.example.infrastructure.EmailClient
import com.example.utils.catchBlockService
import com.example.utils.generateOTP
import com.example.utils.otpStorage
import com.example.utils.verifyPassword

final class AuthService(
    private val accountService: AccountService,
    private val userService: UserService,
    private val emailClient: EmailClient
) {
    suspend fun register(email: String?, password: String?): RegisterResponse {
        return catchBlockService {
            val account = accountService.createAccount(email, password)

            val user = userService.createUser(
                email,
                accountId = account.id,
                gender = 0,
                address = null,
                birthday = null,
                phoneNumber = null,
                avatar = null,
                status = 0
            )

            sendVerifyCode(email)

            val accessToken = JWTConfig.makeOnBoardingJWTToken(userId = user.id)
            val refreshToken = JWTConfig.makeJWTRefreshToken(userId = user.id)

            RegisterResponse(accessToken = accessToken, refreshToken = refreshToken)
        }
    }

    suspend fun login(email: String?, password: String?): LoginResponse {
        return catchBlockService {
            val account = accountService.getAccountByEmail(email)

            if (password.isNullOrEmpty()) {
                throw FastAiException(
                    FastAiException.MISSING_PASSWORD_ERROR_CODE,
                    message = FastAiException.MISSING_PASSWORD_ERROR_MESSAGE
                )
            }

            if (!verifyPassword(password, account.password)) {
                throw FastAiException(
                    FastAiException.INCORRECT_PASSWORD_ERROR_CODE,
                    message = FastAiException.INCORRECT_PASSWORD_ERROR_MESSAGE
                )
            }

            val user = userService.getUserByAccountId(account.id)

            val accessToken = JWTConfig.makeJWTToken(user.id)
            val refreshToken = JWTConfig.makeJWTRefreshToken(user.id)

            LoginResponse(accessToken, refreshToken)
        }
    }

    suspend fun sendVerifyCode(email: String?): Boolean {
        return catchBlockService {

            if (email.isNullOrEmpty()) {
                throw FastAiException(
                    FastAiException.MISSING_EMAIL_ERROR_CODE,
                    FastAiException.MISSING_EMAIL_ERROR_MESSAGE
                )
            }

            val otp = generateOTP()

            otpStorage[email] = otp to System.currentTimeMillis()

            val smtpEmail = ApplicationConfig.getSmtpEmail()

            emailClient.sendEmail(
                to = email,
                from = smtpEmail,
                subject = "Fast AI verification",
                content = "Your otp code: $otp"
            )
        }
    }

    suspend fun registerVerifyEmail(userId: Int?,otpCode: String?, email: String?) : Boolean {
        return catchBlockService {
            if (otpCode.isNullOrEmpty()) {
                throw FastAiException(FastAiException.MISSING_OTP_ERROR_CODE, FastAiException.MISSING_OTP_ERROR_MESSAGE)
            }

            if (email.isNullOrEmpty()) {
                throw FastAiException(
                    FastAiException.MISSING_EMAIL_ERROR_CODE,
                    FastAiException.MISSING_EMAIL_ERROR_MESSAGE
                )
            }

            val storageOtp = otpStorage[email] ?: throw FastAiException(
                FastAiException.OTP_IN_CORRECT_ERROR_CODE,
                FastAiException.OTP_IN_CORRECT_ERROR_MESSAGE
            )

            val (otp, timeStamp) = storageOtp

            // Check if OTP is correct
            if (otp != otpCode) {
                throw FastAiException(
                    FastAiException.OTP_IN_CORRECT_ERROR_CODE,
                    FastAiException.OTP_IN_CORRECT_ERROR_MESSAGE
                )
            }

            // Check if OTP is expired
            val isExpired = (System.currentTimeMillis() - timeStamp) > 15 * 60 * 1000
            if (isExpired) {
                throw FastAiException(
                    FastAiException.OTP_EXPIRED_ERROR_CODE,
                    FastAiException.OTP_EXPIRED_ERROR_MESSAGE
                )
            }

            userService.update(
                id = userId,
                status = 1,
                gender = null,
                address = null,
                birthday = null,
                phoneNumber = null,
                avatar = null,
                userName = null
            )
        }
    }

    suspend fun refreshToken(refreshToken: String?): RefreshResponse {
        return catchBlockService {
            if (refreshToken.isNullOrEmpty()) {
                throw FastAiException(
                    FastAiException.MISSING_REFRESH_TOKEN_ERROR_CODE,
                    FastAiException.MISSING_REFRESH_TOKEN_ERROR_MESSAGE
                )
            }

            val claims = JWTConfig.getClaims(refreshToken)

            if (claims != null) {
                val scope = claims["scope"]?.toString()

                if (scope == "refresh") {
                    val userId = claims["id"]!!.asInt()

                    val accessToken = JWTConfig.makeJWTToken(userId)

                    val newRefreshToken = JWTConfig.makeJWTRefreshToken(userId)

                    RefreshResponse(accessToken, newRefreshToken)
                } else {
                    throw FastAiException(
                        FastAiException.REFRESH_TOKEN_EXPIRED_TOKEN_ERROR_CODE,
                        FastAiException.REFRESH_TOKEN_EXPIRED_TOKEN_ERROR_MESSAGE
                    )
                }
            } else {
                throw FastAiException(
                    FastAiException.REFRESH_TOKEN_EXPIRED_TOKEN_ERROR_CODE,
                    FastAiException.REFRESH_TOKEN_EXPIRED_TOKEN_ERROR_MESSAGE
                )
            }

        }
    }

    suspend fun registerCompleteProfile(
        id: Int?,
        userName: String?,
        gender: Int?,
        phoneNumber: String?,
        avatar: String?,
        address: String?,
        birthday: String?,
    ) : Boolean {
        return catchBlockService {
            userService.update(id,userName, gender, phoneNumber, avatar, address, birthday, 2)
        }
    }
}