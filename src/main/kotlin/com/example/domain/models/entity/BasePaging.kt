package com.example.domain.models.entity

data class BasePaging<T>(val data : T, val currentPage: Int, val totalPages: Long, val pageSize: Int)