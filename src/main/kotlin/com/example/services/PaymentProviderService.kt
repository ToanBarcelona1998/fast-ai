package com.example.services

import com.example.domain.exceptions.FastAiException
import com.example.domain.models.requests.PaymentProviderAddRequest
import com.example.domain.models.responses.GetAllPaymentProviderResponse
import com.example.repository.interfaces.IPaymentProviderRepository
import com.example.utils.catchBlockService

class PaymentProviderService(private val paymentProviderRepository: IPaymentProviderRepository) {

    suspend fun getAll() : GetAllPaymentProviderResponse{
        return catchBlockService {
            val providers = paymentProviderRepository.getAll()

            GetAllPaymentProviderResponse(providers)
        }
    }

    suspend fun add(name: String? , description : String?) : Boolean{
        return catchBlockService {

            if(name.isNullOrEmpty()){
                throw FastAiException(FastAiException.PAYMENT_PROVIDER_MISSING_NAME_ERROR_CODE,FastAiException.PAYMENT_PROVIDER_MISSING_NAME_ERROR_MESSAGE)
            }

            if(paymentProviderRepository.getMethodByName(name) != null){
                throw FastAiException(FastAiException.PAYMENT_PROVIDER_EXISTS_ERROR_CODE, FastAiException.PAYMENT_PROVIDER_EXISTS_ERROR_MESSAGE)
            }

            val request = PaymentProviderAddRequest(name = name , description = description)

            paymentProviderRepository.add(request)

            true
        }
    }

}