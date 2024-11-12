package com.example.domain.models.entity

import com.example.domain.exceptions.FastAiException
import kotlinx.serialization.*

@Serializable
data class User(
    val id: Int,
    val userName: String,
    val phoneNumber: String?,
    val gender: Int,
    val address: String?,
    val birthday: String?,
    val avatar: String?,
    val accountId: Int,
    val updateAt: String?,
    val createAt: String,
    val status: Int
) {
    fun copyWith(
        id: Int?,
        userName: String?,
        phoneNumber: String?,
        gender: Int?,
        address: String?,
        birthday: String?,
        avatar: String?,
        updateAt: String?,
        createAt: String?,
        status: Int?,
    ): User {
        return User(
            id = id ?: this.id,
            userName = userName ?: this.userName,
            phoneNumber = phoneNumber ?: this.phoneNumber,
            gender = gender ?: this.gender,
            address = address ?: this.address,
            birthday = birthday ?: this.birthday,
            avatar = avatar,
            accountId = this.accountId,
            updateAt = updateAt ?: this.updateAt,
            createAt = createAt ?: this.createAt,
            status = status ?: this.status,
        )
    }

    fun checkStatus(): User {

        when (status) {
            0 -> {
                throw FastAiException(FastAiException.USER_ON_WATING_VERIFY_ERROR_CODE,FastAiException.USER_ON_WATING_VERIFY_ERROR_MESSAGE)
            }

            1 -> {
                throw FastAiException(FastAiException.USER_ON_BOARDING_STATUS_ERROR_CODE,FastAiException.USER_ON_BOARDING_STATUS_ERROR_MESSAGE)
            }

            3 -> {
                throw FastAiException(FastAiException.USER_ON_BLOCKING_STATUS_ERROR_CODE,FastAiException.USER_ON_BLOCKING_STATUS_ERROR_MESSAGE)
            }

            4 -> {
                throw FastAiException(FastAiException.USER_ON_DELETING_STATUS_ERROR_CODE,FastAiException.USER_ON_DELETING_STATUS_ERROR_MESSAGE)
            }

            else -> {
                return this
            }
        }
    }
}
