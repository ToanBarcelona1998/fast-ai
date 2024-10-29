package com.example.services

import com.example.domain.exceptions.FastAiException
import com.example.domain.models.requests.PurchaseAddRequest
import com.example.domain.models.responses.CreatePurchaseResponse
import com.example.repository.interfaces.IPurchaseRepository
import com.example.utils.catchBlockService

class PurchaseService(private val purchaseRepository : IPurchaseRepository ,private val packageService: PackageService) {

    suspend fun createTransaction(userId : Int? , methodId : Int? , packageId : Int?) : CreatePurchaseResponse{
        return catchBlockService {

            if(userId == null){
                throw FastAiException(FastAiException.PURCHASE_MISSING_USER_ID_ERROR_CODE,FastAiException.PURCHASE_MISSING_USER_ID_ERROR_MESSAGE)
            }

            if(packageId == null){
                throw FastAiException(FastAiException.PURCHASE_MISSING_PACKAGE_ID_ERROR_CODE , FastAiException.PURCHASE_MISSING_PACKAGE_ID_ERROR_MESSAGE)
            }

            val packageById = packageService.get(packageId)

            if(methodId == null){
                throw FastAiException(FastAiException.PURCHASE_MISSING_METHOD_ID_ERROR_CODE , FastAiException.PURCHASE_MISSING_METHOD_ID_ERROR_MESSAGE)
            }

            val request = PurchaseAddRequest(userId = userId, methodId = methodId , packageId = packageId , creditsPurchased = packageById.credits)

            val createdPurchase = purchaseRepository.add(request)

            CreatePurchaseResponse(createdPurchase)
        }
    }

    suspend fun completePurchase(){
        return catchBlockService {  }
    }
}