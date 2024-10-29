package com.example.repository.interfaces

interface IAddRepository<T ,R> {
    suspend  fun add(request : R) : T
}

interface  IUpdateRepository<R,ID>{
    suspend  fun update(id : ID,request : R) : Boolean
}

interface IDeleteRepository<ID>{
    suspend  fun delete(id : ID) : Boolean
}

interface IGetRepository<T,ID> {
    suspend fun get(id : ID) : T?
}

interface IGetListRepository<T,R> {
    suspend fun getAll() : List<T>

    suspend fun getAll(request: R) : List<T>
}