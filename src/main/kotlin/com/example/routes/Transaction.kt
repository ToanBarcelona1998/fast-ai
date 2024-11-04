package com.example.routes

import com.example.services.PurchaseService
import com.example.utils.claimId
import com.example.utils.parseDataToRespond
import com.example.utils.parseErrorToRespond
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.transactionRoutes(purchaseService: PurchaseService) {
    route("/transaction") {
        authenticate {
            post("/create-transaction") {
                try {
                    val userId = call.claimId()
                    val formData = call.receiveParameters()
                    val response = purchaseService.createTransaction(
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

                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }
        }
    }
}