package com.example.routes

import com.example.services.PaymentProviderService
import com.example.utils.parseDataToRespond
import com.example.utils.parseErrorToRespond
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.paymentProviderRoutes(paymentProviderService: PaymentProviderService){
    route("/payment-provider"){
        authenticate("auth-jwt") {
            get("/") {
                try {
                    val response = paymentProviderService.getAll()

                    call.parseDataToRespond(response)
                }catch (e : Exception){
                    call.parseErrorToRespond(e)
                }
            }

            post("/") {
                try{
                    val formData = call.receiveParameters()

                    val name = formData["method_name"]
                    val description = formData["description"]
                    val type = formData["type"]

                    val status = paymentProviderService.add(name,description , type = type)

                    call.parseDataToRespond(status)
                }catch (e: Exception){
                    call.parseErrorToRespond(e)
                }
            }
        }
    }
}