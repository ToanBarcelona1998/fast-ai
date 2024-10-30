package com.example.repository.interfaces

import com.example.domain.models.entity.Purchase
import com.example.domain.models.requests.PurchaseAddRequest
import com.example.domain.models.requests.PurchaseUpdateRequest
import com.example.domain.models.requests.PurchaseUpdateRequests

interface IPurchaseRepository : IAddRepository<Purchase,PurchaseAddRequest> , IUpdateRepository<PurchaseUpdateRequest,Int> , IGetRepository<Purchase, Int>{
    suspend fun multiUpdate(requests: List<PurchaseUpdateRequests>) : Boolean
}