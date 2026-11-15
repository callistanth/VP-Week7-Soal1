package com.example.w7.model

data class PokemonCard(
    val id: String,
    val name: String,
    val setName: String,
    val cardNumber: String,
    val rarity: String,
    val illustrator: String,
    val type: String,
    val marketPrice: Double?,
    val lowestPrice: Double?,
    val activeListings: Int,
    val verifiedSellers: Int,
    val primaryPrinting: String,
    val imageUrl: String?,
)
