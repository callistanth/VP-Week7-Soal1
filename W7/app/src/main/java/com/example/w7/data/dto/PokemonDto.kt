package com.example.w7.data.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PokemonCardsResponseDto(
    val data: List<PokemonCardDto> = emptyList(),
    val metadata: MetadataDto? = null,
)

@Serializable
data class PokemonCardDto(
    val tcgPlayerId: String? = null,
    val id: String? = null,
    val name: String = "",
    val setName: String? = null,
    val cardNumber: String? = null,
    val rarity: String? = null,
    val artist: String? = null,
    val illustrator: String? = null,
    val supertype: String? = null,
    val subtypes: List<String>? = null,
    val type: String? = null,
    val types: List<String>? = null,
    val energyType: String? = null,
    val prices: CardPricesDto? = null,
    val imageCdnUrl400: String? = null,
    val imageCdnUrl: String? = null,
    val imageUrl: String? = null,
)

@Serializable
data class CardPricesDto(
    val market: Double? = null,
    val low: Double? = null,
    val mid: Double? = null,
    val high: Double? = null,
    val sellers: Int? = null,
    val listings: Int? = null,
    val primaryPrinting: String? = null,
    val lastUpdated: String? = null,
)

@Serializable
data class MetadataDto(
    val total: Int? = null,
    val count: Int? = null,
)
