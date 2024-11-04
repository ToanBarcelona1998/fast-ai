package com.example.services

import com.example.client.Web3Client
import com.example.domain.exceptions.FastAiException
import com.example.domain.models.entity.PaymentType
import com.example.utils.catchBlockService

class TransactionService(
    private val purchaseService: PurchaseService,
    private val userCreditService: UserCreditService,
    private val web3Client: Web3Client
) {
    suspend fun completeTransaction(id: Int?, userId : Int,data : String?,type : String?): Boolean {
        return catchBlockService {

            // Get current transaction by id
            val transaction = purchaseService.get(id)

            if(transaction.isCompleted()){
                throw FastAiException(FastAiException.TRANSACTION_PURCHASE_ALREADY_UPDATE_ERROR_CODE, FastAiException.TRANSACTION_PURCHASE_ALREADY_UPDATE_ERROR_MESSAGE)
            }

            if(type.isNullOrEmpty()){
                throw FastAiException(FastAiException.TRANSACTION_MISSING_METHOD_TYPE_ERROR_CODE, FastAiException.TRANSACTION_MISSING_METHOD_TYPE_ERROR_MESSAGE)
            }

            if(data.isNullOrEmpty()){
                throw FastAiException(FastAiException.TRANSACTION_MISSING_VERIFY_DATA_ERROR_CODE, FastAiException.TRANSACTION_MISSING_VERIFY_DATA_ERROR_MESSAGE)
            }

            // Verify transaction

            val paymentType  = PaymentType.findByType(type)

            val isSupportMethod =  paymentType != null

            if(!isSupportMethod){
                throw FastAiException(FastAiException.TRANSACTION_METOHD_NOT_ALLOWED_ERROR_CODE, FastAiException.TRANSACTION_METOHD_NOT_ALLOWED_ERROR_MESSAGE)
            }

            var verified = false

            when(paymentType!!){
                PaymentType.Web3 -> {
                    verified = web3Client.verifyTxHash(data)
                }
                PaymentType.Google -> {

                }
                PaymentType.Apple -> {

                }
            }



            var status = "completed"

            if(!verified){
                // Update credit
                status = "failed"
            }else{
                userCreditService.update(userId = userId,transaction.creditsPurchased)
            }

             purchaseService.update(id = id, status = status , data)
        }
    }

    suspend fun completeMultiTransaction(){

    }

}