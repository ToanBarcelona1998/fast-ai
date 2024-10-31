package com.example.services

import com.example.domain.exceptions.FastAiException
import com.example.domain.models.requests.ImageAddRequest
import com.example.domain.models.requests.Paging
import com.example.domain.models.responses.CreateImageResponse
import com.example.domain.models.responses.GetHistoryImagesResponse
import com.example.repository.interfaces.IImageGeneratorRepository
import com.example.utils.catchBlockService

class ImageService(private val imageRepository: IImageGeneratorRepository) {
    suspend fun add(userId : Int? , width : Int? , height : Int? , format: String? , path : String?): CreateImageResponse {
        return catchBlockService {

            if(userId == null){
                throw FastAiException(FastAiException.IMAGE_MISSING_USER_ID_ERROR_CODE, FastAiException.IMAGE_MISSING_USER_ID_ERROR_MESSAGE)
            }

            if(format.isNullOrEmpty()){
                throw FastAiException(FastAiException.IMAGE_MISSING_FILE_FOMAT_ERROR_CODE,FastAiException.IMAGE_MISSING_FILE_FOMAT_ERROR_MESSAGE)
            }

            if(path.isNullOrEmpty()){
                throw FastAiException(FastAiException.IMAGE_MISSING_S3_PATH_ERROR_CODE,FastAiException.IMAGE_MISSING_S3_PATH_ERROR_MESSAGE)
            }

            val request = ImageAddRequest(width = width , height = height , fileFormat = format , s3Url = path , userId = userId)

            val image = imageRepository.add(request)

            CreateImageResponse(image)
        }
    }

    suspend fun getHistoryImages(userId : Int?, page : Int? , limit : Int?) : GetHistoryImagesResponse {
        return catchBlockService {
            if(userId == null){
                throw FastAiException(FastAiException.IMAGE_MISSING_USER_ID_ERROR_CODE, FastAiException.IMAGE_MISSING_USER_ID_ERROR_MESSAGE)
            }

            val paging = Paging(offset = page ?: 1 , limit = limit ?: Int.MAX_VALUE)

            val images = imageRepository.getHistoryImages(userId = userId, paging = paging)

            GetHistoryImagesResponse(images)
        }
    }
}