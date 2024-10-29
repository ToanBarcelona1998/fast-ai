package com.example.services

import com.example.domain.exceptions.FastAiException
import com.example.domain.models.entity.Package
import com.example.domain.models.requests.PackageAddRequest
import com.example.domain.models.responses.GetAllPackagesResponse
import com.example.repository.interfaces.IPackageRepository
import com.example.utils.catchBlockService
import java.math.BigDecimal

class PackageService(private val packageRepository: IPackageRepository) {
    suspend fun add(name: String? , description : String? , basePrice : BigDecimal?) : Boolean{
        return catchBlockService {

            if(name.isNullOrEmpty()){
                throw FastAiException(FastAiException.MISSING_PACKAGE_NAME_ERROR_CODE, FastAiException.MISSING_PACKAGE_NAME_ERROR_MESSAGE)
            }

            if(basePrice == null){
                throw FastAiException(FastAiException.MISSING_PACKAGE_BASE_PRICE_ERROR_CODE, FastAiException.MISSING_PACKAGE_BASE_PRICE_ERROR_MESSAGE)
            }

            val request = PackageAddRequest(name = name , credits = 1, description = description , basePrice = basePrice)

            packageRepository.add(request)

            true
        }
    }

    suspend fun getAll() : GetAllPackagesResponse{
        return catchBlockService {
            val packages = packageRepository.getAll()

            GetAllPackagesResponse(packages)
        }
    }

    suspend fun get(id : Int?) : Package{
        return catchBlockService {

            if(id == null){
                throw FastAiException(FastAiException.MISSING_PACKAGE_ID_ERROR_CODE, FastAiException.MISSING_PACKAGE_ID_ERROR_MESSAGE)
            }

            packageRepository.get(id) ?: throw FastAiException(FastAiException.PACKAGE_NOT_FOUND_ERROR_CODE, FastAiException.PACKAGE_NOT_FOUND_ERROR_MESSAGE)
        }
    }
}