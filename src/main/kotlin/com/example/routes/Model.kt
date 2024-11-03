package com.example.routes

import com.example.services.ModelService
import com.example.utils.parseDataToRespond
import com.example.utils.parseErrorToRespond
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.modelRoute(modelService: ModelService){
    route("/model"){
        authenticate {
            post {
                try {
                    val formData = call.receiveParameters()

                    val model = formData["model"]
                    val tags = formData["tags"]
                    val detail = formData["detail"]
                    val type = formData["type"]
                    val thumbnail = formData["thumbnail"]

                    val response = modelService.add(model = model, tags = tags , detail = detail , type = type , thumbnail = thumbnail )

                    call.parseDataToRespond(response)
                }catch (e : Exception){
                    call.parseErrorToRespond(e)
                }
            }

            get("/get-all") {
                try {
                    val response = modelService.getAll()

                    call.parseDataToRespond(response)
                }catch (e : Exception){
                    call.parseErrorToRespond(e)
                }
            }
        }
    }
}