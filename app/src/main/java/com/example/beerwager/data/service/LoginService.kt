package com.example.beerwager.data.service

import com.example.beerwager.data.dtos.LoginDto
import com.example.beerwager.data.dtos.RegisterDto
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginService @Inject constructor(private val client: HttpClient) {

    suspend fun login(loginDto: LoginDto): HttpResponse =
        withContext(Dispatchers.IO) {
            client.post("login") {
                contentType(ContentType.Application.Json)
                setBody(loginDto)
            }
        }

    suspend fun register(registerDto: RegisterDto): HttpResponse =
        withContext(Dispatchers.IO) {
            client.post("register") {
                contentType(ContentType.Application.Json)
                setBody(registerDto)
            }
        }
}