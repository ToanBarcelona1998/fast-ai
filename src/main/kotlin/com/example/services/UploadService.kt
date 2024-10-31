package com.example.services

import com.example.client.AwsS3Client
import com.example.domain.exceptions.FastAiException
import com.example.utils.catchBlockService
import java.io.File

class UploadService (private val awsS3Client: AwsS3Client){
    suspend fun uploadFile(file : File, folder : String) : String{
        return catchBlockService {
            // Limit to 10 MB
            if(file.length() > 10 * 1024 * 104){
                throw FastAiException(FastAiException.UPLOAD_FILE_TOO_LARGE_ERROR_CODE, FastAiException.UPLOAD_FILE_TOO_LARGE_ERROR_MESSAGE)
            }

            val key = "$folder/${file.name}"

            awsS3Client.upload(originKey = key)
        }
    }

    suspend fun multiUpload(files : List<File> , folder : String) : List<String>{
        return catchBlockService {
            val paths = mutableListOf<String>()
            for (file in files){
                val uploadedUrl = uploadFile(file,folder)

                paths.add(uploadedUrl)
            }

            paths
        }
    }
}