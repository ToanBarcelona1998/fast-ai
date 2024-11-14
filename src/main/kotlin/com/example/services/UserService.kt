package com.example.services

import com.example.domain.exceptions.FastAiException
import com.example.domain.models.requests.UserAddRequest
import com.example.domain.models.requests.UserUpdateRequest
import com.example.domain.models.entity.User
import com.example.domain.models.responses.FastAIResponse
import com.example.domain.models.responses.GetHistoryTasksResponse
import com.example.domain.models.responses.GetUserCreditResponse
import com.example.domain.models.responses.GetUserResponse
import com.example.repository.interfaces.IUserRepository
import com.example.utils.catchBlockService

class UserService(private val userRepository: IUserRepository, private val fastAiService: FastAiService, private val taskAiService: AITaskService ,private val userCreditService: UserCreditService) {
    suspend fun createUser(
        userName: String?,
        gender: Int = 0,
        phoneNumber: String?,
        accountId: Int,
        avatar: String?,
        address: String?,
        birthday: String?,
        status: Int,
    ): User {
        return catchBlockService {
            if (userName.isNullOrEmpty()) {
                throw FastAiException(
                    FastAiException.MISSING_USER_NAME_ERROR_CODE,
                    FastAiException.MISSING_USER_NAME_ERROR_MESSAGE
                )
            }

            val request = UserAddRequest(
                userName = userName,
                gender = gender,
                phoneNumber = phoneNumber,
                accountId = accountId,
                avatar = avatar,
                address = address,
                birthday = birthday,
                status = status
            )

            userRepository.add(request)
        }
    }

    suspend fun getUserById(id: Int?): GetUserResponse {
        return catchBlockService {

            if (id == null) {
                throw FastAiException(
                    FastAiException.USER_MISSING_USER_ID_ERROR_CODE,
                    message = FastAiException.USER_MISSING_USER_ID_ERROR_MESSAGE
                )
            }

            val user = userRepository.get(id) ?: throw FastAiException(
                FastAiException.USER_NOT_FOUND_ERROR_CODE,
                FastAiException.USER_NOT_FOUND_ERROR_MESSAGE
            )

            val verifiedUser = user.checkStatus()

            GetUserResponse(verifiedUser)
        }
    }

    suspend fun delete(id: Int): Boolean {
        return true
    }

    suspend fun update(
        id: Int?,
        userName: String?,
        gender: Int?,
        phoneNumber: String?,
        avatar: String?,
        address: String?,
        birthday: String?,
        status: Int?,
    ): Boolean {

        return catchBlockService {
            if (id == null) {
                throw FastAiException(
                    FastAiException.USER_MISSING_USER_ID_ERROR_CODE,
                    message = FastAiException.USER_MISSING_USER_ID_ERROR_MESSAGE
                )
            }

            val request = UserUpdateRequest(
                userName = userName,
                gender = gender,
                phoneNumber = phoneNumber,
                avatar = avatar,
                address = address,
                birthday = birthday,
                status = status,
            )

            userRepository.update(id, request)
        }
    }

    suspend fun getUserByAccountId(id: Int): User {
        return catchBlockService {
            val user = userRepository.getUserByAccountID(id) ?: throw FastAiException(
                FastAiException.USER_BY_ACCOUNT_ID_NOT_FOUND_ERROR_CODE,
                FastAiException.USER_BY_ACCOUNT_ID_NOT_FOUND_ERROR_MESSAGE
            )

            user.checkStatus()
        }
    }

    suspend fun generateImages(
        userId: Int?,
        width: Int?,
        height: Int?,
        model: String?,
        positivePrompt: String?,
        number: Int?,
        negativePrompt: String?,
        seedImage :String?,
        maskImage :String?,
        steps: Int?,
        strength: Float?,
        CFGScale: Int?,
        clipSkip: Int?,
    ): FastAIResponse {
        return fastAiService.generateImages(
            userId,
            width,
            height,
            model,
            positivePrompt,
            number = number,
            negativePrompt = negativePrompt,
            seedImage = seedImage,
            maskImage = maskImage,
            steps = steps,
            strength = strength,
            CFGScale = CFGScale,
            clipSkip = clipSkip,
        )
    }

    suspend fun removeBackgroundImage(userId: Int?, inputImage: String?): FastAIResponse {
        return fastAiService.removeBackground(userId, inputImage)
    }

    suspend fun upscaleImage(userId: Int?, inputImage: String?, scaleFactor: Int?): FastAIResponse {
        return fastAiService.upScaleImage(userId, inputImage, scaleFactor)
    }

    suspend fun imageToText(userId: Int?, inputImage: String?): FastAIResponse {
        return fastAiService.imageToText(userId, inputImage)
    }

    suspend fun controlNetProcessor(
        userId: Int?,
        inputImage: String?,
        preProcessorType: String?,
        width: Int?,
        height: Int?
    ): FastAIResponse {
        return fastAiService.controlNetProcessor(
            userId = userId,
            inputImage = inputImage,
            preProcessorType = preProcessorType,
            width = width,
            height = height
        )
    }

    suspend fun enhancePrompt(userId: Int?, prompt: String? , promptMaxLength : Int?) : FastAIResponse{
        return fastAiService.enhancePrompt(userId, prompt, promptMaxLength)
    }

    suspend fun getTasksHistory(userId: Int?, page :Int? , limit : Int?) : GetHistoryTasksResponse{
        return taskAiService.getHistoryImages(userId,page, limit)
    }

    suspend fun getUserCredit(userId: Int?) : GetUserCreditResponse{
        return userCreditService.getUserCredit(userId)
    }
}