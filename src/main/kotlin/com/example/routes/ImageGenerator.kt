package com.example.routes

import com.example.services.UserService
import com.example.utils.claimId
import com.example.utils.parseDataToRespond
import com.example.utils.parseErrorToRespond
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

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
                        CFGScale = formData["CFGScale"]?.toInt(),
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
                    val inputImage = call.receiveParameters()["image"]
                    val response = userService.removeBackgroundImage(userId = userId, inputImage = inputImage)
                    call.parseDataToRespond(response)
                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }

            post("/upscale") {
                try {
                    val userId = call.claimId()
                    val formData = call.receiveParameters()
                    val response = userService.upscaleImage(
                        userId = userId,
                        inputImage = formData["image"],
                        scaleFactor = formData["scaleFactor"]?.toInt()
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
                    val response = userService.imageToText(userId = userId, inputImage = call.receiveParameters()["input_image"])
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