package com.example.w7.data.repository

import com.example.w7.data.remote.PokemonApi
import com.example.w7.data.remote.ServiceLocator
import com.example.w7.model.PokemonCard
import com.example.w7.util.safeCall
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

class PokemonRepository(private val api: PokemonApi) {

    private val cachedCards = mutableMapOf<String, PokemonCard>()

    suspend fun searchCards(query: String): Result<List<PokemonCard>> = guarded {
        val response = api.searchCards(search = query, limit = 20)
        val domainList = response.data.map { it.toDomain() }
        domainList.forEach { cachedCards[it.id] = it }
        domainList
    }

    suspend fun getCardDetail(cardId: String): Result<PokemonCard> = guarded {
        cachedCards[cardId]?.let { return@guarded it }
        val response = api.getCardById(tcgPlayerId = cardId)
        val card = response.data.firstOrNull()?.toDomain()
            ?: throw NoSuchElementException("Kartu dengan ID $cardId tidak ditemukan.")
        cachedCards[card.id] = card
        card
    }

    private suspend fun <T> guarded(block: suspend () -> T): Result<T> {
        val key = ServiceLocator.getApiKey()
        if (key.isBlank()) {
            val responseBody = "HTTP 401 Unauthorized: API Key belum diisi. Masukkan API Key di local.properties atau ServiceLocator.kt"
                .toResponseBody("text/plain".toMediaType())
            return Result.failure(HttpException(Response.error<Any>(401, responseBody)))
        }
        return safeCall(block)
    }
}
