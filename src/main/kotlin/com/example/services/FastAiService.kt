package com.example.services

import com.example.client.FastAiClient
import com.example.domain.exceptions.FastAiException
import com.example.domain.models.entity.Image
import com.example.domain.models.requests.ImageTaskRequest
import com.example.domain.models.requests.RemoveImageBackgroundTaskRequest
import com.example.domain.models.responses.GenerateImagesResponse
import com.example.domain.models.responses.RemoveBackgroundImageResponse
import com.example.utils.catchBlockService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class FastAiService(private val fastAiClient: FastAiClient, private val uploadService: UploadService ,private val userCreditService: UserCreditService ,private val imageService: ImageService , bucketName : String) {

    private val defaultWidth = 512
    private val defaultHeight = 512
    private val s3URL = "https://$bucketName.s3.amazonaws.com/images/"

    suspend fun generateImages(userId : Int?,width: Int?, height: Int?, model: String?, positivePrompt: String?, number: Int?) : GenerateImagesResponse {
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

            checkUserCredit(userId)

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

            userCreditService.update(userId = userId , creditChange = -(number ?: 1))

            val listImage = data.data

            val images = mutableListOf<Image>()

            for (image in listImage) {
                if (taskId != image.taskUUID) {
                    throw FastAiException(
                        FastAiException.RUNWARE_INVALID_TASK_ERROR_CODE,
                        FastAiException.RUNWARE_INVALID_TASK_ERROR_MESSAGE
                    )
                }

                uploadToS3(image.imageBase64Data , image.imageUUID)

                val addImageResponse = imageService.add(userId = userId,width = width ?: defaultWidth , height = height ?: defaultHeight , path = image.imageUUID , format = "png")


                val imageCopy = addImageResponse.image.copy(
                    s3Url = s3URL + image.imageUUID + ".png"
                )

                images.add(imageCopy)
            }

            GenerateImagesResponse(images)
        }
    }

    suspend fun removeBackground(userId : Int? , inputImage : String?) : RemoveBackgroundImageResponse{
        return catchBlockService {
            checkUserCredit(userId)

            if(inputImage.isNullOrEmpty()){
                throw FastAiException(FastAiException.RUNWARE_MISSING_INPUT_IMAGE_ERROR_CODE,FastAiException.RUNWARE_MISSING_INPUT_IMAGE_ERROR_MESSAGE)
            }

            val taskId = UUID.randomUUID().toString()

            val removeBgRequest = RemoveImageBackgroundTaskRequest(taskUUID = taskId , inputImage = inputImage)

            val data = fastAiClient.removeBackground(removeBgRequest)

            userCreditService.update(userId = userId , -1)

            val listImage = data.data

            val images = mutableListOf<Image>()

            for (image in listImage) {
                if (taskId != image.taskUUID) {
                    throw FastAiException(
                        FastAiException.RUNWARE_INVALID_TASK_ERROR_CODE,
                        FastAiException.RUNWARE_INVALID_TASK_ERROR_MESSAGE
                    )
                }

                uploadToS3(image.imageBase64Data , image.imageUUID)

                val addImageResponse = imageService.add(userId = userId,width = null , height = null , path = image.imageUUID , format = "png")

                val imageCopy = addImageResponse.image.copy(
                    s3Url = s3URL + image.imageUUID + ".png"
                )

                images.add(imageCopy)
            }

            RemoveBackgroundImageResponse(images)
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

        val response = uploadService.uploadFile(tempFile, "images")

        tempFile.delete()


        return response.url
    }
}