package com.example.routes

import com.example.services.UploadService
import com.example.utils.parseDataToRespond
import com.example.utils.parseErrorToRespond
import io.ktor.http.content.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.io.readByteArray
import java.io.File

fun Route.uploadRoute(uploadService: UploadService){
    route("/upload"){
        authenticate {
            post {
                try {
                    val parts = call.receiveMultipart()

                    val files = mutableListOf<File>()

                    parts.forEachPart { part ->
                        when(part) {
                            is PartData.FileItem -> {
                                val fileName = part.originalFileName as String

                                val fileBytes = part.provider().readRemaining().readByteArray()

                                val file = File(fileName)

                                file.writeBytes(fileBytes)

                                files.add(file)
                            }
                            else -> {

                            }
                        }
                        part.dispose()
                    }

                    val response = uploadService.multiUpload(files,"uploads" , contentType = "image/png")

                    files.forEach{ file->
                        file.delete()
                    }

                    call.parseDataToRespond(response)
                }catch (e : Exception){
                    call.parseErrorToRespond(e)
                }
            }
        }
    }
}