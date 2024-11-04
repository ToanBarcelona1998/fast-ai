package com.example.services

import com.example.client.FastAiClient
import com.example.domain.exceptions.FastAiException
import com.example.domain.models.requests.*
import com.example.domain.models.responses.FastAIResponse
import com.example.utils.catchBlockService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class FastAiService(
    private val fastAiClient: FastAiClient,
    private val uploadService: UploadService,
    private val userCreditService: UserCreditService,
    private val taskService: AITaskService,
    bucketName: String
) {

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
    ): FastAIResponse {
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

            request.validate()

            val data = fastAiClient.generateImages(request)

            if (data.data.isEmpty()) {
                throw FastAiException(
                    FastAiException.RUNWARE_DEVELOP_ERROR_CODE,
                    FastAiException.RUNWARE_DEVELOP_ERROR_MESSAGE
                )
            }

            userCreditService.update(userId = userId, creditChange = -(number ?: 1))

            val tasks = data.data.map { task ->
                validateTaskId(taskId, task.taskUUID)

                val url = uploadToS3(task.imageBase64Data, task.imageUUID)

                val rawData = mutableMapOf<String, Any>()

                rawData["width"] = width ?: defaultWidth
                rawData["height"] = width ?: defaultHeight
                rawData["format"] = "png"

                val addImageResponse = taskService.add(
                    userId = userId,
                    data = url.replaceBefore("/", ""),
                    rawData = rawData.toString(),
                    taskType = ETaskType.INFERENCE.type
                )

                addImageResponse.task.copy(
                    data = url
                )
            }

            FastAIResponse(tasks)
        }
    }

    suspend fun removeBackground(userId: Int?, inputImage: String?): FastAIResponse {
        return catchBlockService {
            checkUserCredit(userId)

            if (inputImage.isNullOrEmpty()) {
                throw FastAiException(
                    FastAiException.RUNWARE_MISSING_INPUT_IMAGE_ERROR_CODE,
                    FastAiException.RUNWARE_MISSING_INPUT_IMAGE_ERROR_MESSAGE
                )
            }

            val taskId = UUID.randomUUID().toString()

            val removeBgRequest = RemoveImageBackgroundTaskRequest(taskUUID = taskId, inputImage = inputImage)
            val data = fastAiClient.removeBackground(removeBgRequest)

            if (data.data.isEmpty()) {
                throw FastAiException(
                    FastAiException.RUNWARE_DEVELOP_ERROR_CODE,
                    FastAiException.RUNWARE_DEVELOP_ERROR_MESSAGE
                )
            }

            userCreditService.update(userId = userId, creditChange = -1)

            val tasks = data.data.map { task ->
                validateTaskId(taskId, task.taskUUID)

                val url = uploadToS3(task.imageBase64Data, task.imageUUID)

                val addTaskResponse = taskService.add(
                    userId = userId,
                    rawData = null,
                    data = url.replaceBefore("/", ""),
                    taskType = ETaskType.REMOVE_BG.type
                )

                addTaskResponse.task.copy(
                    data = url
                )
            }

            FastAIResponse(tasks)
        }
    }

    suspend fun upScaleImage(userID: Int?, inputImage: String?, upScaleFactor: Int?): FastAIResponse {
        return catchBlockService {
            checkUserCredit(userID)

            if (inputImage.isNullOrEmpty()) {
                throw FastAiException(
                    FastAiException.RUNWARE_MISSING_INPUT_IMAGE_ERROR_CODE,
                    FastAiException.RUNWARE_MISSING_INPUT_IMAGE_ERROR_MESSAGE
                )
            }

            val upScaleSize = upScaleFactor ?: 2

            val taskId = UUID.randomUUID().toString()

            val request = UpScaleGanTaskRequest(inputImage = inputImage, taskUUID = taskId, upscaleFactor = upScaleSize)

            request.validate()

            val data = fastAiClient.upScaleImage(request)

            val tasks = data.data.map { task ->
                validateTaskId(taskId, task.taskUUID)

                val url = uploadToS3(task.imageBase64Data, task.imageUUID)

                val rawData = mutableMapOf<String, Any>()

                rawData["upscaleFactor"] = upScaleSize
                rawData["format"] = "png"

                val addTaskResponse = taskService.add(
                    userId = userID,
                    taskType = ETaskType.UPSCALE.type,
                    data = url.replaceBefore("/", ""),
                    rawData = rawData.toString()
                )

                addTaskResponse.task.copy(
                    data = url
                )
            }

            FastAIResponse(tasks)
        }
    }

    suspend fun enhancePrompt(userId: Int?, prompt: String?, promptMaxLength: Int?): FastAIResponse {
        return catchBlockService {
            checkUserCredit(userId = userId)

            val taskId = UUID.randomUUID().toString()

            if (prompt.isNullOrEmpty()) {
                throw FastAiException(
                    FastAiException.RUNWARE_MISSING_PROMPT_ERROR_CODE,
                    FastAiException.RUNWARE_MISSING_PROMPT_ERROR_MESSAGE
                )
            }

            val enhancePromptTaskRequest =
                EnhancePromptTaskRequest(taskUUID = taskId, prompt = prompt, promptMaxLength = promptMaxLength ?: 64)

            val fastAIData = fastAiClient.enhancePrompt(request = enhancePromptTaskRequest)

            val tasks = fastAIData.data.map { task ->
                validateTaskId(taskId, task.taskUUID)

                taskService.add(
                    userId = userId,
                    taskType = ETaskType.PROMPT_ENHANCE.type,
                    data = task.text,
                    rawData = null
                ).task
            }

            FastAIResponse(tasks)

        }
    }

    suspend fun controlNetProcessor(
        userId: Int?,
        inputImage: String?,
        width: Int?,
        height: Int?,
        preProcessorType: String?
    ) : FastAIResponse {
        return catchBlockService {

            checkUserCredit(userId = userId)

            if (inputImage.isNullOrEmpty()) {
                throw FastAiException(
                    FastAiException.RUNWARE_MISSING_INPUT_IMAGE_ERROR_CODE,
                    FastAiException.RUNWARE_MISSING_INPUT_IMAGE_ERROR_MESSAGE
                )
            }

            if (preProcessorType.isNullOrEmpty()) {
                throw FastAiException(
                    FastAiException.RUNWARE_MISSING_PRE_PROCESSOR_TYPE_ERROR_CODE,
                    FastAiException.RUNWARE_MISSING_PRE_PROCESSOR_TYPE_ERROR_MESSAGE
                )
            }

            val taskId = UUID.randomUUID().toString()

            val controlNetPreprocessTaskRequest =
                ControlNetPreprocessTaskRequest(
                    taskUUID = taskId,
                    inputImage = inputImage,
                    width = width ?: defaultWidth,
                    height = height ?: defaultHeight,
                    preProcessorType = preProcessorType
                )

            controlNetPreprocessTaskRequest.validate()

            val fastAIData = fastAiClient.controlNetProcessor(request = controlNetPreprocessTaskRequest)

            val tasks = fastAIData.data.map { task ->
                validateTaskId(taskId,task.taskUUID)

                val url = uploadToS3(task.guideImageBase64Data, task.inputImageUUID)

                val addTaskResponse = taskService.add(
                    userId = userId,
                    taskType = ETaskType.CONTROL_NET.type,
                    data = url.replaceBefore("/", ""),
                    rawData = null
                )

                addTaskResponse.task.copy(
                    data = url
                )

            }

            FastAIResponse(tasks)
        }
    }

    suspend fun imageToText(userId : Int? , inputImage: String?) : FastAIResponse{
        return catchBlockService {

            checkUserCredit(userId = userId)

            if(inputImage.isNullOrEmpty()){
                throw FastAiException(FastAiException.RUNWARE_MISSING_INPUT_IMAGE_ERROR_CODE , FastAiException.RUNWARE_MISSING_INPUT_IMAGE_ERROR_MESSAGE)
            }

            val taskId = UUID.randomUUID().toString()

            val request = ImageToTextTaskRequest(inputImage = inputImage , taskUUID = taskId)

            val imageToTextResponse = fastAiClient.imageToText(request)

            val tasks = imageToTextResponse.data.map { task ->
                validateTaskId(taskId,task.taskUUID)

                taskService.add(
                    userId = userId,
                    taskType = ETaskType.CONTROL_NET.type,
                    data = task.text,
                    rawData = null
                ).task
            }

            FastAIResponse(tasks)
        }
    }

    private suspend fun checkUserCredit(userId: Int?) {
        val remainingCredit = userCreditService.getUserCredit(userId)
        if (remainingCredit.userCredit.remaining <= 0) {
            throw FastAiException(
                FastAiException.RUNWARE_USER_NOT_ENOUGH_TIME_ERROR_CODE,
                FastAiException.RUNWARE_USER_NOT_ENOUGH_TIME_ERROR_MESSAGE
            )
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