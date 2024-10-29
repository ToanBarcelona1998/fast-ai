package com.example.routes

import com.example.domain.models.BaseResponseSuccessful
import com.example.services.UserService
import com.example.utils.claimId
import com.example.utils.parseErrorToRespond
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes(userService: UserService) {
    route("/user") {
        authenticate {
            get {
                try {
                    val id = claimId(call)

                    val user = userService.getUserById(id)

                    call.respond(
                        status = HttpStatusCode.OK,
                        BaseResponseSuccessful(data = user)
                    )
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
        }
    }
}