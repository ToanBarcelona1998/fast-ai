package com.example.services

import com.example.domain.exceptions.FastAiException
import com.example.domain.models.entity.Purchase
import com.example.domain.models.entity.PurchaseStatus
import com.example.domain.models.requests.PurchaseAddRequest
import com.example.domain.models.requests.PurchaseUpdateRequest
import com.example.domain.models.requests.PurchaseUpdateRequests
import com.example.domain.models.responses.CreatePurchaseResponse
import com.example.repository.interfaces.IPurchaseRepository
import com.example.utils.catchBlockService
import kotlinx.serialization.json.Json

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

    suspend fun update(id: Int? , status: String ? , data : String?) : Boolean{
        return catchBlockService {

            if(id == null){
                throw FastAiException(FastAiException.PURCHASE_MISSING_ID_ERROR_CODE,FastAiException.PURCHASE_MISSING_ID_ERROR_MESSAGE)
            }

            if(status.isNullOrEmpty()){
                throw FastAiException(FastAiException.PURCHASE_MISSING_STATUS_ERROR_CODE, FastAiException.PURCHASE_MISSING_STATUS_ERROR_MESSAGE)
            }


            if(!PurchaseStatus.isAccessed(status)){
                throw FastAiException(FastAiException.PURCHASE_STATUS_NOT_BE_ACCEPTED_ERROR_CODE, FastAiException.PURCHASE_STATUS_NOT_BE_ACCEPTED_ERROR_MESSAGE)
            }

            val request = PurchaseUpdateRequest(status = status, data = data)

            purchaseRepository.update(id,request)
        }
    }

    suspend fun multiUpdateTransaction(requests : String?) : Boolean{
        return catchBlockService {

            if(requests.isNullOrEmpty()){
                throw FastAiException(FastAiException.PURCHASE_MISSING_MULTI_REQUEST_ERROR_CODE,FastAiException.PURCHASE_MISSING_MULTI_REQUEST_ERROR_MESSAGE)
            }

            val parsedRequest : List<PurchaseUpdateRequests> = Json.decodeFromString(requests)

            purchaseRepository.multiUpdate(parsedRequest)
        }
    }

    suspend fun get(id : Int?) : Purchase{
        return catchBlockService {

            if(id == null){
                throw FastAiException(FastAiException.PURCHASE_MISSING_ID_ERROR_CODE, FastAiException.PURCHASE_MISSING_ID_ERROR_MESSAGE)
            }

            purchaseRepository.get(id) ?: throw FastAiException(FastAiException.PURCHASE_PURCHASE_NOT_FOUND_ERROR_CODE, FastAiException.PURCHASE_PURCHASE_NOT_FOUND_ERROR_MESSAGE)
        }
    }
}