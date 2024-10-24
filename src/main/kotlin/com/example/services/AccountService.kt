package com.example.services

import com.example.domain.exceptions.FastAiException
import com.example.domain.models.requests.AccountAddRequest
import com.example.domain.models.entity.Account
import com.example.repository.interfaces.IAccountRepository
import com.example.utils.catchBlockService
import com.example.utils.hashPassword
import com.example.utils.validateEmail

final class AccountService(private val accountRepository: IAccountRepository) {
    suspend fun createAccount(email: String?, password: String?): Account {
        return catchBlockService {
            if (email.isNullOrEmpty()) {
                throw FastAiException(
                    FastAiException.MISSING_EMAIL_ERROR_CODE,
                    message = FastAiException.MISSING_EMAIL_ERROR_MESSAGE
                )
            }

            if (password.isNullOrEmpty()) {
                throw FastAiException(
                    FastAiException.MISSING_PASSWORD_ERROR_CODE,
                    message = FastAiException.MISSING_PASSWORD_ERROR_MESSAGE
                )
            }

            if (!validateEmail(email)) {
                throw FastAiException(
                    FastAiException.EMAIL_INVALID_ERROR_CODE,
                    message = FastAiException.EMAIL_INVALID_ERROR_MESSAGE
                )
            }

            val isExistsAccount = accountRepository.getAccountByEmail(email) != null

            if (isExistsAccount) {
                throw FastAiException(
                    FastAiException.ACCOUNT_EXISTS_ERROR_CODE,
                    message = FastAiException.ACCOUNT_EXISTS_ERROR_MESSAGE
                )
            }

            val hashedPassword = hashPassword(password)

            val request = AccountAddRequest(email, hashedPassword)

            accountRepository.add(request)
        }
    }

    suspend fun getAccountByEmail(email: String?): Account {
        return catchBlockService {
            if (email.isNullOrEmpty()) {
                throw FastAiException(
                    FastAiException.MISSING_EMAIL_ERROR_CODE,
                    message = FastAiException.MISSING_EMAIL_ERROR_MESSAGE
                )
            }

            if (!validateEmail(email)) {
                throw FastAiException(
                    FastAiException.EMAIL_INVALID_ERROR_CODE,
                    message = FastAiException.EMAIL_INVALID_ERROR_MESSAGE
                )
            }

            accountRepository.getAccountByEmail(email) ?: throw FastAiException(
                FastAiException.ACCOUNT_NOT_EXISTS_ERROR_CODE,
                message = FastAiException.ACCOUNT_NOT_EXISTS_ERROR_MESSAGE
            )
        }
    }
}