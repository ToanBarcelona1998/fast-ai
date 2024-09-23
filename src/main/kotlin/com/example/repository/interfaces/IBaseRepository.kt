package com.example.repository.interfaces

interface IBaseRepository<T> {
    suspend  fun add(entity : T) : T

    suspend  fun delete(id : Int) : Boolean

    suspend  fun update(id : Int,entity : T) : Boolean

    suspend fun get(id : Int) : T?
}