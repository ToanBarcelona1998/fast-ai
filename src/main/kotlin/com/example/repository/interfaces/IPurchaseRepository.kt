package com.example.repository.interfaces

import com.example.domain.models.entity.Purchase
import com.example.domain.models.requests.PurchaseAddRequest

interface IPurchaseRepository : IAddRepository<Purchase,PurchaseAddRequest>{
}