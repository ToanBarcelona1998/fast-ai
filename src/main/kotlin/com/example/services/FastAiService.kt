package com.example.services

import com.example.client.FastAiClient
import com.example.domain.exceptions.FastAiException
import com.example.domain.models.entity.Image
import com.example.domain.models.requests.ImageTaskRequest
import com.example.domain.models.requests.RemoveImageBackgroundTaskRequest
import com.example.domain.models.requests.UpScaleGanTaskRequest
import com.example.domain.models.responses.GenerateImagesResponse
import com.example.domain.models.responses.RemoveBackgroundImageResponse
import com.example.domain.models.responses.UpscaleImageResponse
import com.example.utils.catchBlockService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class FastAiService(private val fastAiClient: FastAiClient, private val uploadService: UploadService ,private val userCreditService: UserCreditService ,private val imageService: ImageService , bucketName : String) {

    private val defaultWidth = 512
    private val defaultHeight = 512
    private val s3URL = "https://$bucketName.s3.ap-southeast-2.amazonaws.com/images/"

    suspend fun generateImages(
        userId: Int?,
        width: Int? = defaultWidth,
        height: Int? = defaultHeight,
        model: String?,
        positivePrompt: String?,
        negativePrompt: String? = null,
        number: Int? = 1
    ): GenerateImagesResponse {
        return catchBlockService {
            validateGenerateImagesInputs(model, positivePrompt)

            checkUserCredit(userId)

            val taskId = UUID.randomUUID().toString()

            val request = ImageTaskRequest(
                taskUUID = taskId,
                model = model!!,
                positivePrompt = positivePrompt!!,
                width = width ?: defaultWidth,
                height = height ?: defaultHeight,
                numberResults = number ?: 1,
                negativePrompt = negativePrompt
            )

            val data = fastAiClient.generateImages(request)

            if (data.data.isEmpty()) {
                throw FastAiException(FastAiException.RUNWARE_DEVELOP_ERROR_CODE, FastAiException.RUNWARE_DEVELOP_ERROR_MESSAGE)
            }

            userCreditService.update(userId = userId, creditChange = -(number ?: 1))

            val images = data.data.map { image ->
                validateTaskId(taskId, image.taskUUID)

                val url = uploadToS3(image.imageBase64Data, image.imageUUID)

                val addImageResponse = imageService.add(
                    userId = userId,
                    width = width ?: defaultWidth,
                    height = height ?: defaultHeight,
                    path = url.replaceBefore("/" ,""),
                    format = "png"
                )

                addImageResponse.image.copy(
                    s3Url = url.replaceBefore("/" ,"")
                )
            }

            GenerateImagesResponse(images)
        }
    }

    suspend fun removeBackground(userId: Int?, inputImage: String?): RemoveBackgroundImageResponse {
        return catchBlockService {
            checkUserCredit(userId)

            if (inputImage.isNullOrEmpty()) {
                throw FastAiException(FastAiException.RUNWARE_MISSING_INPUT_IMAGE_ERROR_CODE, FastAiException.RUNWARE_MISSING_INPUT_IMAGE_ERROR_MESSAGE)
            }

            val taskId = UUID.randomUUID().toString()

            val removeBgRequest = RemoveImageBackgroundTaskRequest(taskUUID = taskId, inputImage = inputImage)
            val data = fastAiClient.removeBackground(removeBgRequest)

            if (data.data.isEmpty()) {
                throw FastAiException(FastAiException.RUNWARE_DEVELOP_ERROR_CODE, FastAiException.RUNWARE_DEVELOP_ERROR_MESSAGE)
            }

            userCreditService.update(userId = userId, creditChange = -1)

            val images = data.data.map { image ->
                validateTaskId(taskId, image.taskUUID)

                val url = uploadToS3(image.imageBase64Data, image.imageUUID)

                val addImageResponse = imageService.add(
                    userId = userId,
                    width = null,
                    height = null,
                    path = url.replaceBefore("/" ,""),
                    format = "png"
                )

                addImageResponse.image.copy(
                    s3Url = url.replaceBefore("/" ,"")
                )
            }

            RemoveBackgroundImageResponse(images)
        }
    }

    suspend fun upScaleImage(userID : Int? ,inputImage: String?, upScaleFactor : Int?): UpscaleImageResponse{
        return catchBlockService {
            checkUserCredit(userID)

            if (inputImage.isNullOrEmpty()) {
                throw FastAiException(FastAiException.RUNWARE_MISSING_INPUT_IMAGE_ERROR_CODE, FastAiException.RUNWARE_MISSING_INPUT_IMAGE_ERROR_MESSAGE)
            }

            var upScaleSize = upScaleFactor ?:2

            if(upScaleSize > 4 || upScaleSize < 2){
                throw FastAiException(FastAiException.RUNWARE_UP_SCALE_FACTOR_OVER_ERROR_CODE , FastAiException.RUNWARE_UP_SCALE_FACTOR_OVER_ERROR_MESSAGE)
            }

            val taskId = UUID.randomUUID().toString()

            val request = UpScaleGanTaskRequest(inputImage = inputImage , taskUUID = taskId , upscaleFactor = upScaleSize)

            val data = fastAiClient.upScaleImage(request)

            val images = data.data.map { image ->
                validateTaskId(taskId, image.taskUUID)

                val url = uploadToS3(image.imageBase64Data, image.imageUUID)

                val addImageResponse = imageService.add(
                    userId = userID,
                    width = null,
                    height = null,
                    path = url.replaceBefore("/" ,""),
                    format = "png"
                )

                addImageResponse.image.copy(
                    s3Url = url.replaceBefore("/" ,"")
                )
            }

            UpscaleImageResponse(images)
        }
    }

    private suspend fun checkUserCredit(userId: Int?) {
        val remainingCredit = userCreditService.getUserCredit(userId)
        if (remainingCredit.userCredit.remaining <= 0) {
            throw FastAiException(FastAiException.RUNWARE_USER_NOT_ENOUGH_TIME_ERROR_CODE, FastAiException.RUNWARE_USER_NOT_ENOUGH_TIME_ERROR_MESSAGE)
        }
    }

    private suspend fun uploadToS3(imgBase64: String, imgName: String): String {
        val base64Data = imgBase64.replace("^data:image/\\w+;base64,".toRegex(), "")
        val bytes = Base64.getDecoder().decode(base64Data)

        val tempFile = withContext(Dispatchers.IO) {
            File.createTempFile("fast-ai", imgName)
        }.apply {
            writeBytes(bytes)
        }

        val response = uploadService.uploadFile(tempFile, "image/png", folder = "images")
        tempFile.delete() // Clean up the temporary file

        return response.url
    }

    private fun validateGenerateImagesInputs(model: String?, positivePrompt: String?) {
        if (model.isNullOrEmpty()) {
            throw FastAiException(
                FastAiException.RUNWARE_MISSING_MODEL_ERROR_CODE,
                FastAiException.RUNWARE_MISSING_MODEL_ERROR_MESSAGE
            )
        }
        if (positivePrompt.isNullOrEmpty()) {
            throw FastAiException(
                FastAiException.RUNWARE_MISSING_PROMPT_ERROR_CODE,
                FastAiException.RUNWARE_MISSING_PROMPT_ERROR_MESSAGE
            )
        }
    }

    private fun validateTaskId(expectedTaskId: String, actualTaskId: String) {
        if (expectedTaskId != actualTaskId) {
            throw FastAiException(
                FastAiException.RUNWARE_INVALID_TASK_ERROR_CODE,
                FastAiException.RUNWARE_INVALID_TASK_ERROR_MESSAGE
            )
        }
    }
}