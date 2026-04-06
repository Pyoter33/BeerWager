package com.example.beerwager.domain.use_case

import com.example.beerwager.data.dtos.RegisterDto
import com.example.beerwager.data.service.LoginService
import com.example.beerwager.utils.toBodyOrError
import kotlinx.io.IOException
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterUseCase @Inject constructor(private val loginService: LoginService) {

    suspend operator fun invoke(registerDto: RegisterDto): Result<String> {
        try {
            val result = loginService.register(registerDto)
            result.toBodyOrError<Unit>()
            return Result.success(result.headers["Authorization"] ?: throw IllegalStateException())
        } catch (e: IOException) {
            Timber.e(e)
            return Result.failure(e)
        }
    }
}