package com.example.services

import com.example.domain.exceptions.FastAiException
import com.example.domain.models.requests.UserAddRequest
import com.example.domain.models.requests.UserUpdateRequest
import com.example.domain.models.entity.User
import com.example.domain.models.responses.GetUserResponse
import com.example.repository.interfaces.IUserRepository
import com.example.utils.catchBlockService

class UserService(private val userRepository: IUserRepository) {
    suspend fun createUser(
        userName: String?,
        gender: Int = 0,
        phoneNumber: String?,
        accountId: Int,
        avatar: String?,
        address: String?,
        birthday: String?,
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
                birthday = birthday
            )

            userRepository.add(request)
        }
    }

    suspend fun getUserById(id: Int?): GetUserResponse {
        return catchBlockService {

            if (id == null) {
                throw FastAiException(
                    FastAiException.GET_USER_MISSING_USER_ID_ERROR_CODE,
                    message = FastAiException.GET_USER_MISSING_USER_ID_ERROR_MESSAGE
                )
            }

            val user = userRepository.get(id) ?: throw FastAiException(
                FastAiException.USER_NOT_FOUND_ERROR_CODE,
                FastAiException.USER_NOT_FOUND_ERROR_MESSAGE
            )

            GetUserResponse(user)
        }
    }

    suspend fun delete(id: Int): Boolean {
        return true
    }

    suspend fun update(
        id: Int, userName: String?,
        gender: Int = 0,
        phoneNumber: String?,
        avatar: String?,
        address: String?,
        birthday: String?,
        isActive: Boolean?,
    ): User {
        return catchBlockService {
            val request = UserUpdateRequest(
                userName = userName,
                gender = gender,
                phoneNumber = phoneNumber,
                avatar = avatar,
                address = address,
                birthday = birthday,
                isActive = isActive,
            )

            userRepository.update(id, request)
        }
    }

    suspend fun getUserByAccountId(id: Int): User {
        return catchBlockService {
            userRepository.getUserByAccountID(id) ?: throw FastAiException(
                FastAiException.USER_BY_ACCOUNT_ID_NOT_FOUND_ERROR_CODE,
                FastAiException.USER_BY_ACCOUNT_ID_NOT_FOUND_ERROR_MESSAGE
            )
        }
    }
}