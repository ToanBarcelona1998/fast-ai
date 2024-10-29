package com.example.routes

import com.example.domain.models.BaseResponseSuccessful
import com.example.services.UserCreditService
import com.example.services.UserService
import com.example.utils.claimId
import com.example.utils.parseDataToRespond
import com.example.utils.parseErrorToRespond
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.userRoutes(userService: UserService , userCreditService: UserCreditService) {
    route("/user") {
        authenticate {
            get {
                try {
                    val id = claimId(call)

                    val response = userService.getUserById(id)

                    parseDataToRespond(response,call)
                } catch (e: Exception) {
                    parseErrorToRespond(e, call)
                }
            }

            put {
                try {
                    val id = claimId(call)

                    val formData = call.receiveParameters()

                    val userName = formData["user_name"]
                    val phoneNumber = formData["phone_number"]
                    val birthday = formData["birthday"]
                    val address = formData["address"]
                    val avatar = formData["avatar"]
                    val status = formData["status"]?.toInt()
                    val gender = formData["gender"]?.toInt()

                    userService.update(
                        id = id,
                        userName = userName,
                        phoneNumber = phoneNumber,
                        gender = gender,
                        birthday = birthday,
                        address = address,
                        avatar = avatar,
                        status = status
                    )

                } catch (e: Exception) {
                    parseErrorToRespond(e, call)
                }
            }

            get("/credit") {
                try{
                    val id = claimId(call)

                    val response = userCreditService.getUserCredit(id)

                    parseDataToRespond(response,call)
                }catch (e: Exception){
                    parseErrorToRespond(e,call)
                }
            }
        }
    }
}