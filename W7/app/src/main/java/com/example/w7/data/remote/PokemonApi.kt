package com.example.w7.data.remote

import com.example.w7.data.dto.PokemonCardsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApi {

    @GET("api/v2/cards")
    suspend fun searchCards(
        @Query("search") search: String,
        @Query("limit") limit: Int = 20,
    ): PokemonCardsResponseDto

    @GET("api/v2/cards")
    suspend fun getCardById(
        @Query("tcgPlayerId") tcgPlayerId: String,
    ): PokemonCardsResponseDto
}
