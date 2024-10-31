package com.example.repository.interfaces

import com.example.domain.models.entity.Image
import com.example.domain.models.requests.ImageAddRequest
import com.example.domain.models.requests.Paging

interface IImageGeneratorRepository : IAddRepository<Image,ImageAddRequest>{
    suspend fun getHistoryImages(userId: Int, paging: Paging) : List<Image>
}