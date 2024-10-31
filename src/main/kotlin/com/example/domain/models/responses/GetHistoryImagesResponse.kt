package com.example.domain.models.responses

import com.example.domain.models.entity.Image
import kotlinx.serialization.*

@Serializable
class GetHistoryImagesResponse(val images : List<Image>)