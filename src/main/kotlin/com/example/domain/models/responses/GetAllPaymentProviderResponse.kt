package com.example.domain.models.responses

import com.example.domain.models.entity.PaymentProvider
import kotlinx.serialization.*

@Serializable
data class GetAllPaymentProviderResponse(val providers : List<PaymentProvider>)
