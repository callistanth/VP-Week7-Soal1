package com.example.w7.util

import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import retrofit2.HttpException

suspend fun <T> safeCall(block: suspend () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

fun Throwable.toUserMessage(): String = when (this) {
    is HttpException -> when (code()) {
        401, 429 -> "HTTP 401"
        404 -> "HTTP 404: Not Found"
        else -> "HTTP ${code()}: Server Error"
    }
    is IOException -> "HTTP 503: Connection Failed"
    else -> message ?: "Unknown Error"
}
