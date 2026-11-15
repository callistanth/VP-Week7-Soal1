package com.example.w7.navigation

import kotlinx.serialization.Serializable

@Serializable
data object PokemonListRoute

@Serializable
data class PokemonDetailRoute(val cardId: String)
