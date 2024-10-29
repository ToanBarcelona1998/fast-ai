package com.example.routes

import com.example.services.PackageService
import com.example.utils.parseDataToRespond
import com.example.utils.parseErrorToRespond
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import java.math.BigDecimal

fun Route.packageRoutes(packageService: PackageService){
    route("/packages"){
        authenticate {
            get("/") {
                try{
                    val getAllPackagesResponse = packageService.getAll()

                    parseDataToRespond(getAllPackagesResponse,call)
                }catch (e : Exception){
                    parseErrorToRespond(e,call)
                }
            }

            post("/") {
                try{
                    val formData = call.receiveParameters()

                    val name = formData["package_name"]

                    val description = formData["description"]

                    val basePrice = formData["base_price"] ?: "0.0"

                    val status = packageService.add(name = name , description = description, basePrice = BigDecimal(basePrice))

                    parseDataToRespond(status,call)
                }catch (e : Exception){
                    parseErrorToRespond(e,call)
                }
            }
        }
    }
}