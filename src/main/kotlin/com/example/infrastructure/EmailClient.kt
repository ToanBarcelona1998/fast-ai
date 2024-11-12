package com.example.infrastructure

import org.simplejavamail.api.mailer.Mailer
import org.simplejavamail.email.EmailBuilder

final class EmailClient(private val mailer: Mailer) {
    fun sendEmail(from: String, to: String, content: String, subject: String): Boolean {
        return try {
            val email = EmailBuilder
                .startingBlank()
                .from(from)
                .to(to)
                .withSubject(subject)
                .withPlainText(content)
                .buildEmail()

            mailer.sendMail(email)

            true
        } catch (e: Exception) {
            false
        }
    }
}