package com.example.services

import com.example.client.FastAiClient
import com.example.domain.exceptions.FastAiException
import com.example.domain.models.requests.ImageTaskRequest
import com.example.domain.models.requests.RemoveImageBackgroundTaskRequest
import com.example.utils.catchBlockService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class FastAiService(private val fastAiClient: FastAiClient, private val uploadService: UploadService ,private val userCreditService: UserCreditService) {

    private val defaultWidth = 512
    private val defaultHeight = 512

    suspend fun generateImages(userId : Int?,width: Int?, height: Int?, model: String?, positivePrompt: String?, number: Int?) {
        return catchBlockService {

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

            val remainingCredit = userCreditService.getUserCredit(userId)

            if(remainingCredit.userCredit.remaining <= 0){
                throw FastAiException(FastAiException.RUNWARE_USER_NOT_ENOUGH_TIME_ERROR_CODE,FastAiException.RUNWARE_USER_NOT_ENOUGH_TIME_ERROR_MESSAGE)
            }

            val taskId = UUID.randomUUID().toString()

            val request = ImageTaskRequest(
                taskUUID = taskId,
                model = model,
                positivePrompt = positivePrompt,
                width = width ?: defaultWidth,
                height = height ?: defaultHeight,
                numberResults = number ?: 1
            )

            val data = fastAiClient.generateImages(request)

            val listImage = data.data

            for (image in listImage) {
                if (taskId != image.taskUUID) {
                    throw FastAiException(
                        FastAiException.RUNWARE_INVALID_TASK_ERROR_CODE,
                        FastAiException.RUNWARE_INVALID_TASK_ERROR_MESSAGE
                    )
                }

                var url = uploadToS3(image.imageBase64Data , image.imageUUID)
            }

            userCreditService.update(userId = userId , -1)
        }
    }

    suspend fun removeBackground(userId : Int? , inputImage : String?){
        return catchBlockService {
            checkUserCredit(userId)

            if(inputImage.isNullOrEmpty()){
                throw FastAiException(FastAiException.RUNWARE_MISSING_INPUT_IMAGE_ERROR_CODE,FastAiException.RUNWARE_MISSING_INPUT_IMAGE_ERROR_MESSAGE)
            }

            val taskId = UUID.randomUUID().toString()

            val removeBgRequest = RemoveImageBackgroundTaskRequest(taskUUID = taskId , inputImage = inputImage)

            val data = fastAiClient.removeBackground(removeBgRequest)

            val listImage = data.data


            for (image in listImage) {
                if (taskId != image.taskUUID) {
                    throw FastAiException(
                        FastAiException.RUNWARE_INVALID_TASK_ERROR_CODE,
                        FastAiException.RUNWARE_INVALID_TASK_ERROR_MESSAGE
                    )
                }

                var url = uploadToS3(image.imageBase64Data , image.imageUUID)
            }

            userCreditService.update(userId = userId , -1)
        }
    }

    suspend fun upScaleImage(){
        return catchBlockService {

        }
    }

    private suspend fun checkUserCredit(userId : Int?){
        val remainingCredit = userCreditService.getUserCredit(userId)

        if(remainingCredit.userCredit.remaining <= 0){
            throw FastAiException(FastAiException.RUNWARE_USER_NOT_ENOUGH_TIME_ERROR_CODE,FastAiException.RUNWARE_USER_NOT_ENOUGH_TIME_ERROR_MESSAGE)
        }
    }

    private suspend fun uploadToS3(imgBase64 : String , imgName : String) : String{
        val bytes = Base64.getDecoder().decode(imgBase64)

        val tempFile = withContext(Dispatchers.IO) {
            File.createTempFile("upload-", imgName)
        }

        tempFile.writeBytes(bytes)

        val url = uploadService.uploadFile(tempFile, "images")

        tempFile.delete()

        return url
    }
}