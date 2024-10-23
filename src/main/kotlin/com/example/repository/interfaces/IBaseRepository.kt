package com.example.repository.interfaces

interface IAddRepository<T ,R> {
    suspend  fun add(request : R) : T
}

interface  IUpdateRepository<T,R,ID>{
    suspend  fun update(id : ID,request : R) : T
}

interface IDeleteRepository<ID>{
    suspend  fun delete(id : ID) : Boolean
}

interface IGetRepository<T,ID> {
    suspend fun get(id : ID) : T?
}