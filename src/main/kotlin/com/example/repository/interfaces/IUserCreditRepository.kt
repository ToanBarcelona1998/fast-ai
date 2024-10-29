package com.example.repository.interfaces

import com.example.domain.models.entity.UserCredit
import com.example.domain.models.requests.UserCreditUpdateRequest

interface IUserCreditRepository : IUpdateRepository<UserCredit,UserCreditUpdateRequest,Int>{
    suspend fun getCreditByUserId(id : Int) : UserCredit?
}