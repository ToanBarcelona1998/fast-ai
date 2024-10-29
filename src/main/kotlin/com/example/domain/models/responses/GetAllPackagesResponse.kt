package com.example.domain.models.responses

import com.example.domain.models.entity.Package

import kotlinx.serialization.*

@Serializable
data class GetAllPackagesResponse(private val packages : List<Package>)