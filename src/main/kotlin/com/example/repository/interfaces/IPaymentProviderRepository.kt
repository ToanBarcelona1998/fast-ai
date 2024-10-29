package com.example.repository.interfaces

import com.example.domain.models.entity.PaymentProvider
import com.example.domain.models.requests.PaymentProviderAddRequest

interface IPaymentProviderRepository : IAddRepository<PaymentProvider,PaymentProviderAddRequest> , IGetListRepository<PaymentProvider , Int> {
    suspend fun getMethodByName(name : String) : PaymentProvider?
}