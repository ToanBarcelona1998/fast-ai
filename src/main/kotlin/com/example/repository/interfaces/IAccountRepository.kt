package com.example.repository.interfaces

import com.example.domain.models.requests.AccountAddRequest
import com.example.domain.models.requests.AccountUpdateRequest
import com.example.domain.models.entity.Account

interface IAccountRepository : IAddRepository<Account,AccountAddRequest> , IDeleteRepository<Int>, IUpdateRepository<Account,AccountUpdateRequest,Int>, IGetRepository<Account,Int>{
    suspend fun existsByEmail(email : String) : Boolean

    suspend fun getAccountIDByEmailAndPassword(email : String , password: String) : Int?
}