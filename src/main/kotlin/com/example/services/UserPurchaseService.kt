package com.example.services

import com.example.domain.exceptions.FastAiException
import com.example.repository.interfaces.ITransactionRepository
import com.example.utils.catchBlockService

class UserPurchaseService(
    private val purchaseService: PurchaseService,
    private val userCreditService: UserCreditService,
    private val iTransactionRepository: ITransactionRepository
) {
    suspend fun completeTransaction(id: Int?, userId : Int,data : String?,): Boolean {
        return catchBlockService {

            // Get current transaction by id
            val transaction = purchaseService.get(id)

            if(transaction.isCompleted()){
                throw FastAiException(FastAiException.PURCHASE_PURCHASE_ALREADY_UPDATE_ERROR_CODE, FastAiException.PURCHASE_PURCHASE_ALREADY_UPDATE_ERROR_MESSAGE)
            }

            // Verify transaction
            val verified = iTransactionRepository.verifyTransaction(data ?: "")

            var status = "completed"

            if(verified){
                // Update credit
                status = "failed"

                userCreditService.update(userId = userId,transaction.creditsPurchased)
            }

             purchaseService.update(id = id, status = status , data)
        }
    }

    suspend fun completeMultiTransaction(){
        
    }

}