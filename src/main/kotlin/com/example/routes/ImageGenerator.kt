package com.example.routes

import com.example.services.UserService
import com.example.utils.claimId
import com.example.utils.parseDataToRespond
import com.example.utils.parseErrorToRespond
import io.ktor.http.content.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.io.readByteArray
import java.util.Base64

fun Route.imageGeneratorRoutes(userService: UserService) {
    route("/image-generator") {
        authenticate("auth-jwt") {
            post("/generate-images") {
                try {
                    val userId = call.claimId()
                    val formData = call.receiveParameters()
                    val response = userService.generateImages(
                        userId = userId,
                        width = formData["width"]?.toInt(),
                        height = formData["height"]?.toInt(),
                        model = formData["model"],
                        positivePrompt = formData["prompt"],
                        negativePrompt = formData["negativePrompt"],
                        number = formData["number"]?.toInt(),
                        CFGScale = formData["CFGScale"]?.toFloat(),
                        strength = formData["strength"]?.toFloat(),
                        seedImage = formData["seed_image"],
                        maskImage = formData["mask_image"],
                        steps = formData["steps"]?.toInt(),
                        clipSkip = formData["clipSkip"]?.toInt()
                    )
                    call.parseDataToRespond(response)
                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }

            post("/remove-background") {
                try {
                    val userId = call.claimId()
                    val parts = call.receiveMultipart(1024 * 1024 * 10)

                    var inputImage : String? = null

                    parts.forEachPart { part ->
                        when(part) {
                            is PartData.FileItem -> {
                                if(part.name == "file"){
                                    val fileBytes = part.provider().readRemaining().readByteArray()

                                    inputImage = Base64.getEncoder().encodeToString(fileBytes)

                                    return@forEachPart
                                }
                            }
                            else -> {

                            }
                        }
                        part.dispose()
                    }

                    val response = userService.removeBackgroundImage(userId = userId, inputImage = inputImage)
                    call.parseDataToRespond(response)
                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }

            post("/upscale") {
                try {
                    val userId = call.claimId()
                    val parts = call.receiveMultipart(1024 * 1024 * 10)

                    var inputImage : String? = null
                    var scaleFactor : Int ? = null

                    parts.forEachPart { part ->
                        when(part) {
                            is PartData.FileItem -> {
                                if(part.name == "file"){
                                    val fileBytes = part.provider().readRemaining().readByteArray()

                                    inputImage = Base64.getEncoder().encodeToString(fileBytes)

                                    return@forEachPart
                                }
                            }
                            is PartData.FormItem -> {
                                if(part.name =="scaleFactor"){
                                    scaleFactor = part.value.toInt()
                                }
                            }
                            else -> {

                            }
                        }
                        part.dispose()
                    }
                    val response = userService.upscaleImage(
                        userId = userId,
                        inputImage = inputImage,
                        scaleFactor = scaleFactor
                    )
                    call.parseDataToRespond(response)
                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }

            post("/control-net") {
                try {
                    val userId = call.claimId()
                    val formData = call.receiveParameters()
                    val response = userService.controlNetProcessor(
                        userId = userId,
                        inputImage = formData["input_image"],
                        preProcessorType = formData["type"],
                        width = formData["width"]?.toInt(),
                        height = formData["height"]?.toInt()
                    )
                    call.parseDataToRespond(response)
                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }

            post("/image-to-text") {
                try {
                    val userId = call.claimId()

                    val parts = call.receiveMultipart(1024 * 1024 * 10)

                    var inputImage : String? = null

                    parts.forEachPart { part ->
                        when(part) {
                            is PartData.FileItem -> {
                                if(part.name == "file"){
                                    val fileBytes = part.provider().readRemaining().readByteArray()

                                    inputImage = Base64.getEncoder().encodeToString(fileBytes)

                                    return@forEachPart
                                }
                            }
                            else -> {

                            }
                        }
                        part.dispose()
                    }

                    val response = userService.imageToText(userId = userId, inputImage = inputImage)
                    call.parseDataToRespond(response)
                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }

            post("/enhance-prompt") {
                try {
                    val userId = call.claimId()
                    val formData = call.receiveParameters()
                    val response = userService.enhancePrompt(
                        userId = userId,
                        prompt = formData["prompt"],
                        promptMaxLength = formData["prompt_max_length"]?.toInt()
                    )
                    call.parseDataToRespond(response)
                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }
        }
    }
}