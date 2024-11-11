package com.example.services

import com.example.infrastructure.AwsS3Client
import com.example.domain.exceptions.FastAiException
import com.example.domain.models.responses.UploadFileResponse
import com.example.domain.models.responses.UploadMultiFileResponse
import com.example.utils.catchBlockService
import java.io.File

class UploadService (private val awsS3Client: AwsS3Client){
    suspend fun uploadFile(file : File,contentType : String  ,folder : String) : UploadFileResponse{
        return catchBlockService {
            // Limit to 10 MB
            if(file.length() > 10 * 1024 * 104){
                throw FastAiException(FastAiException.UPLOAD_FILE_TOO_LARGE_ERROR_CODE, FastAiException.UPLOAD_FILE_TOO_LARGE_ERROR_MESSAGE)
            }

            val key = "$folder/${file.name}"

            val url = awsS3Client.upload(originKey = key , file = file.readBytes() , uContentType = contentType)

            UploadFileResponse(url)
        }
    }

    suspend fun multiUpload(files : List<File> , folder : String , contentType : String ) : UploadMultiFileResponse{
        return catchBlockService {
            val paths = mutableListOf<String>()
            for (file in files){
                val response = uploadFile(file = file,folder = folder, contentType = contentType)

                paths.add(response.url)
            }

            UploadMultiFileResponse(paths)
        }
    }
}