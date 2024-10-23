package com.example.services

import com.example.domain.exceptions.FastAiException
import com.example.domain.models.requests.UserAddRequest
import com.example.domain.models.requests.UserUpdateRequest
import com.example.domain.models.entity.User
import com.example.repository.interfaces.IUserRepository

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
        return try {
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
        } catch (e: Exception) {
            throw FastAiException(FastAiException.DATABASE_ERROR_CODE, e.message!!)
        } catch (e: FastAiException) {
            throw e
        }
    }

    suspend fun getUserById(id: Int): User {
        return try {
            userRepository.get(id) ?: throw FastAiException(
                FastAiException.USER_NOT_FOUND_ERROR_CODE,
                FastAiException.USER_NOT_FOUND_ERROR_MESSAGE
            )
        } catch (e: Exception) {
            throw FastAiException(FastAiException.DATABASE_ERROR_CODE, e.message!!)
        } catch (e: FastAiException) {
            throw e
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
        return try {
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
        } catch (e: Exception) {

            throw FastAiException(FastAiException.DATABASE_ERROR_CODE, e.message!!)
        }
    }
}