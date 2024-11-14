package com.example.routes

import com.example.services.AuthService
import com.example.utils.claimId
import com.example.utils.parseDataToRespond
import com.example.utils.parseErrorToRespond
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.authRoutes(authService: AuthService) {
    route("/auth") {
        post("/login") {
            val formData = call.receiveParameters()

            val userName = formData["user_name"]

            val password = formData["password"]

            try {
                val loginResponse = authService.login(email = userName, password = password)

                call.parseDataToRespond(loginResponse)
            } catch (e: Exception) {
                call.parseErrorToRespond(e)
            }
        }

        post("/register") {
            val formData = call.receiveParameters()

            val userName = formData["user_name"]

            val password = formData["password"]
            try {
                val registerResponse = authService.register(email = userName, password = password)

                call.parseDataToRespond(registerResponse)
            } catch (e: Exception) {
                call.parseErrorToRespond(e)
            }
        }

        post("/send-email") {
            val formData = call.receiveParameters()

            val email = formData["email"]

            try {
                val status = authService.sendVerifyCode(email)

                call.parseDataToRespond(status)
            } catch (e: Exception) {
                call.parseErrorToRespond(e)
            }
        }

        post("/refresh-token") {
            val formData = call.receiveParameters()

            val refreshToken = formData["refresh_token"]

            try {
                val response = authService.refreshToken(refreshToken)

                call.parseDataToRespond(response)
            } catch (e: Exception) {
                call.parseErrorToRespond(e)
            }
        }

        authenticate("onboarding-auth") {
            post("/register/verify-email") {
                val userId = call.claimId()

                val formData = call.receiveParameters()

                val otpCode = formData["otp"]

                val email = formData["email"]

                try {
                    val status = authService.registerVerifyEmail(userId,otpCode, email)

                    call.parseDataToRespond(status)
                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }

            post("/register/complete-profile") {
                val formData = call.receiveParameters()

                val userId = call.claimId()

                val userName = formData["user_name"]

                val avatar = formData["avatar"]

                val gender = formData["gender"]?.toInt()

                val address = formData["address"]

                val birthday = formData["birthday"]

                val phoneNumber = formData["phone_number"]

                try {
                    val status = authService.registerCompleteProfile(
                        id = userId,
                        userName = userName,
                        avatar = avatar,
                        gender = gender,
                        address = address,
                        birthday = birthday,
                        phoneNumber = phoneNumber
                    )

                    call.parseDataToRespond(status)
                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }
        }
    }
}