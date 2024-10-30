package com.example.routes

import com.example.services.PurchaseService
import com.example.services.UserCreditService
import com.example.services.UserService
import com.example.utils.claimId
import com.example.utils.parseDataToRespond
import com.example.utils.parseErrorToRespond
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.userRoutes(userService: UserService, userCreditService: UserCreditService, purchaseService: PurchaseService) {
    route("/user") {
        authenticate {
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

            get("/credit") {
                try {
                    val id = call.claimId()

                    val response = userCreditService.getUserCredit(id)

                    call.parseDataToRespond(response)
                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }

            post("/purchase") {
                try {
                    val userId = call.claimId()

                    val formData = call.receiveParameters()

                    val methodId = formData["method_id"]?.toInt()
                    val packageId = formData["package_id"]?.toInt()

                    val response =
                        purchaseService.createTransaction(userId = userId, methodId = methodId, packageId = packageId)

                    call.parseDataToRespond(response)
                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }

//            put("/purchase") {
//                try {
//                    val formData = call.receiveParameters()
//
//                    val id = formData["id"]?.toInt()
//                    val status = formData["status"]
//                    val data = formData["data"]
//
//                    val statusResponse = purchaseService.update(id= id , status = status , data = data)
//
//                    call.parseDataToRespond(statusResponse)
//                } catch (e: Exception) {
//                    call.parseErrorToRespond(e)
//                }
//            }
        }
    }
}