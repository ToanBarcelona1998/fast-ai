package com.example.domain.models.responses

import com.example.domain.models.entity.PaymentProvider

data class GetAllPaymentProviderResponse(val providers : List<PaymentProvider>)
