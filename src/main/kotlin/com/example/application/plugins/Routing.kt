package com.example.application.plugins

import com.example.domain.models.BaseResponseError
import com.example.routes.*
import com.example.services.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call , status ->
            call.respond(HttpStatusCode.OK , message = BaseResponseError(message = HttpStatusCode.NotFound.description , data = null , code = status.value))
        }

        status(HttpStatusCode.Unauthorized){ call , status ->
            call.respond(HttpStatusCode.OK , message = BaseResponseError(message = HttpStatusCode.Unauthorized.description , data = null , code = status.value))
        }

        status(HttpStatusCode.InternalServerError){ call , status ->
            call.respond(HttpStatusCode.OK , message = BaseResponseError(message = HttpStatusCode.InternalServerError.description , data = null , code = status.value))
        }

    }

    // Get service by injection
    val authService : AuthService by inject()

    val userService : UserService by inject()

    val packageService : PackageService by inject()

    val userCreditService : UserCreditService by inject()

    val paymentProviderService : PaymentProviderService by inject()

    val purchaseService : PurchaseService by inject()

    val uploadService : UploadService by inject()

    routing {
        userRoutes(userService,userCreditService , purchaseService)
        authRoutes(authService)
        packageRoutes(packageService)
        paymentProviderRoutes(paymentProviderService)
        uploadRoute(uploadService)
        test()
    }

}
