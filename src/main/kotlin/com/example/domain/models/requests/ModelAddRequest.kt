package com.example.domain.models.requests

class ModelAddRequest(
    val model: String,
    val type: String,
    val tags: String,
    val thumbnail: String,
    val detail: String?
)