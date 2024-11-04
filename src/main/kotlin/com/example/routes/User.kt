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


            post("/image-generator/generate-images") {
                try {

                    val userId = call.claimId()

                    val formData = call.receiveParameters()

                    val width = formData["width"]?.toInt()
                    val height = formData["height"]?.toInt()
                    val model = formData["model"]
                    val positivePrompt = formData["prompt"]
                    val negativePrompt = formData["negativePrompt"]
                    val number = formData["number"]?.toInt()
                    val CFGScale = formData["CFGScale"]?.toInt()
                    val steps = formData["steps"]?.toInt()
                    val clipSkip = formData["steps"]?.toInt()
                    val strength = formData["strength"]?.toFloat()
                    val seedImage = formData["seed_image"]
                    val maskImage = formData["mask_image"]

                    val response = userService.generateImages(
                        userId = userId,
                        width = width,
                        height = height,
                        model = model,
                        positivePrompt = positivePrompt,
                        number = number,
                        negativePrompt = negativePrompt,
                        CFGScale = CFGScale,
                        strength = strength,
                        seedImage = seedImage,
                        maskImage = maskImage,
                        steps = steps,
                        clipSkip = clipSkip
                    )

                    call.parseDataToRespond(response)
                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }

            post("/image-generator/remove-background") {
                try {
                    val userId = call.claimId()

                    val formData = call.receiveParameters()
                    val inputImage = formData["image"]

                    val response = userService.removeBackgroundImage(userId = userId, inputImage = inputImage)

                    call.parseDataToRespond(response)
                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }

            post("/image-generator/upscale") {
                try {
                    val userId = call.claimId()

                    val formData = call.receiveParameters()
                    val inputImage = formData["image"]
                    val scaleFactor = formData["scaleFactor"]?.toInt()

                    val response =
                        userService.upscaleImage(userId = userId, inputImage = inputImage, scaleFactor = scaleFactor)

                    call.parseDataToRespond(response)
                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }

            post("/image-generator/control-net") {
                try {
                    val userId = call.claimId()

                    val formData = call.receiveParameters()

                    val inputImage = formData["input_image"]
                    val preProcessorType = formData["type"]
                    val width = formData["width"]?.toInt()
                    val height = formData["height"]?.toInt()

                    val response = userService.controlNetProcessor(
                        userId = userId,
                        inputImage = inputImage,
                        preProcessorType = preProcessorType,
                        width = width,
                        height = height
                    )

                    call.parseDataToRespond(response)
                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }

            post("/image-generator/image-to-text") {
                try {
                    val userId = call.claimId()

                    val formData = call.receiveParameters()

                    val inputImage = formData["input_image"]

                    val response = userService.imageToText(userId = userId, inputImage = inputImage)

                    call.parseDataToRespond(response)
                } catch (e: Exception) {
                    call.parseErrorToRespond(e)
                }
            }

            post("image-generator/enhance-prompt") {
                try {
                    val userId = call.claimId()

                    val formData = call.receiveParameters()

                    val prompt = formData["prompt"]
                    val promptMaxLength = formData["prompt_max_length"]?.toInt()

                    val response =
                        userService.enhancePrompt(userId = userId, prompt = prompt, promptMaxLength = promptMaxLength)

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
    }
}