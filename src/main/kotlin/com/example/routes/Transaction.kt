package com.example.routes

import com.example.services.TransactionService
import com.example.utils.claimId
import com.example.utils.parseDataToRespond
import com.example.utils.parseErrorToRespond
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.transactionRoutes(transactionService: TransactionService) {
    route("/transaction") {
        authenticate("auth-jwt") {
            post("/create-transaction") {
                try {
                    val userId = call.claimId()
                    val formData = call.receiveParameters()
                    val response = transactionService.createTransaction(
                        userId = userId,
                        methodId = formData["method_id"]?.toInt(),
                        packageId = formData["package_id"]?.toInt()
                    )
                    call.parseDataToRespond(response)
                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }

            post("/complete-transaction") {
                try {
                    val userId = call.claimId()

                    val formData = call.receiveParameters()
                    val id = formData["transaction_id"]?.toInt()
                    val paymentType = formData["payment_type"]
                    val data = formData["verify_data"]

                    transactionService.completeTransaction(userId = userId , id =  id , type = paymentType , data = data)

                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }
        }
    }
}