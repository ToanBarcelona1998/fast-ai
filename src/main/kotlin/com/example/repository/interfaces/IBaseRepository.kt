package com.example.repository.interfaces

interface IBaseRepository {
    fun <T> add(param : T)

    fun <T> delete(id : T)

    fun <T> update(param : T)

    fun <T> get(id : Int) : T
}