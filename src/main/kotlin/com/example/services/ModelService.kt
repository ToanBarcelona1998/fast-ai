package com.example.services

import com.example.domain.exceptions.FastAiException
import com.example.domain.models.entity.Model
import com.example.domain.models.requests.ModelAddRequest
import com.example.domain.models.responses.CreateModelResponse
import com.example.domain.models.responses.GetAllModelResponse
import com.example.repository.interfaces.IModelRepository
import com.example.utils.catchBlockService

class ModelService(private val modelRepository: IModelRepository){
    suspend fun add(model: String?, tags: String?, type: String?, detail: String?, thumbnail: String?): CreateModelResponse {
        return catchBlockService {

            if (model.isNullOrEmpty()) {
                throw FastAiException(FastAiException.MODEL_MISSING_MODEL_ERROR_CODE , FastAiException.MODEL_MISSING_MODEL_ERROR_MESSAGE)
            }

            if(type.isNullOrEmpty()){
                throw FastAiException(FastAiException.MODEL_MISSING_TYPE_ERROR_CODE , FastAiException.MODEL_MISSING_TYPE_ERROR_MESSAGE)
            }

            if(tags.isNullOrEmpty()){
                throw FastAiException(FastAiException.MODEL_MISSING_TAGS_ERROR_CODE , FastAiException.MODEL_MISSING_TAGS_ERROR_MESSAGE)
            }

            if(thumbnail.isNullOrEmpty()){
                throw FastAiException(FastAiException.MODEL_MISSING_THUMB_ERROR_CODE , FastAiException.MODEL_MISSING_THUMB_ERROR_MESSAGE)
            }

            val request =
                ModelAddRequest(model = model, tags = tags, type = type, detail = detail, thumbnail = thumbnail)

            val aiModel =  modelRepository.add(request)

            CreateModelResponse(aiModel)
        }
    }

    suspend fun getAll() : GetAllModelResponse{
        return catchBlockService {
            val models = modelRepository.getAll()

            GetAllModelResponse(models)
        }
    }
}