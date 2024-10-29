package com.example.services

import com.example.domain.exceptions.FastAiException
import com.example.domain.models.entity.UserCredit
import com.example.domain.models.requests.UserCreditUpdateRequest
import com.example.domain.models.responses.GetUserCreditResponse
import com.example.repository.interfaces.IUserCreditRepository
import com.example.utils.catchBlockService

class UserCreditService(private val userCreditRepository: IUserCreditRepository) {
    suspend fun getUserCredit(userId:  Int?) : GetUserCreditResponse{
        return catchBlockService {

            if(userId == null){
                throw FastAiException(FastAiException.USER_CREDIT_MISSING_USER_ID_ERROR_CODE , FastAiException.USER_CREDIT_MISSING_USER_ID_ERROR_MESSAGE)
            }

            val userCredit = userCreditRepository.getCreditByUserId(userId) ?: throw FastAiException(FastAiException.USER_CREDIT_NOT_FOUND_ERROR_CODE,FastAiException.USER_CREDIT_NOT_FOUND_ERROR_MESSAGE)

            GetUserCreditResponse(userCredit)
        }
    }

    suspend fun update(userId : Int?, creditChange : Int?) : Boolean{
        return catchBlockService {

            if(userId == null){
                throw FastAiException(FastAiException.USER_CREDIT_MISSING_USER_ID_ERROR_CODE , FastAiException.USER_CREDIT_MISSING_USER_ID_ERROR_MESSAGE)
            }

            if(creditChange == null){
                throw FastAiException(FastAiException.USER_CREDIT_MISSING_CREDIT_CHANGE_ERROR_CODE,FastAiException.USER_CREDIT_MISSING_CREDIT_CHANGE_ERROR_MESSAGE)
            }

            val request = UserCreditUpdateRequest(creditChange)

            userCreditRepository.update(userId,request)

            true
        }
    }
}