package com.example.beerwager.domain.use_case

import com.example.beerwager.data.dtos.LoginDto
import com.example.beerwager.data.service.LoginService
import com.example.beerwager.utils.toBodyOrError
import kotlinx.io.IOException
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(private val loginService: LoginService) {

    suspend operator fun invoke(loginDto: LoginDto): Result<String> {
        try {
            val result = loginService.login(loginDto)
            result.toBodyOrError<Unit>()
            return Result.success(result.headers["Authorization"] ?: throw IllegalStateException())
        } catch (e: IOException) {
            Timber.e(e)
            return Result.failure(e)
        }
    }
}