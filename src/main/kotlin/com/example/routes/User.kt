package com.example.routes

import com.example.services.UserService
import com.example.utils.claimId
import com.example.utils.parseDataToRespond
import com.example.utils.parseErrorToRespond
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

private suspend inline fun update(call : RoutingCall , userService: UserService){
    try {
        val id = call.claimId()

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
        call.parseErrorToRespond(e)
    }
}

fun Route.userRoutes(userService: UserService) {
    route("/user") {
        authenticate("auth-jwt") {
            get {
                try {
                    val id = call.claimId()

                    val response = userService.getUserById(id)

                    call.parseDataToRespond(response)
                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }

            put {
                update(call,userService)
            }

            get("/credits") {
                try {
                    val id = call.claimId()

                    val response = userService.getUserCredit(id)

                    call.parseDataToRespond(response)
                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }

            get("tasks-history") {
                try{
                    val userId = call.claimId()

                    val parameter = call.queryParameters

                    val page = parameter["page"]?.toInt()

                    val limit = parameter["page_size"]?.toInt()

                    val response = userService.getTasksHistory(userId = userId , page , limit)

                    call.parseDataToRespond(response)
                }catch (e: Exception){
                    call.parseErrorToRespond(e)
                }
            }
        }

        authenticate("onboarding-auth") {
            put {
                update(call,userService)
            }
        }
    }
}