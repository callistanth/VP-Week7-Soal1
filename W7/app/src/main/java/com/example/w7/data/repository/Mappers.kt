package com.example.w7.data.repository

import com.example.w7.data.dto.PokemonCardDto
import com.example.w7.model.PokemonCard

fun PokemonCardDto.toDomain(): PokemonCard {
    val cardId = tcgPlayerId ?: id ?: name
    val image = imageCdnUrl400 ?: imageCdnUrl ?: imageUrl

    val detectedType = types?.firstOrNull()?.takeIf { !it.equals("Pokémon", ignoreCase = true) }
        ?: type?.takeIf { !it.equals("Pokémon", ignoreCase = true) }
        ?: energyType
        ?: inferPokemonType(name)

    val detectedIllustrator = illustrator ?: artist ?: "Eske Yoshinob"

    val detectedCardNumber = cardNumber?.takeIf { it.isNotBlank() }
        ?: extractCardNumberFromName(name)
        ?: cardId

    return PokemonCard(
        id = cardId,
        name = name,
        setName = setName ?: "XY Promos",
        cardNumber = detectedCardNumber,
        rarity = rarity ?: "Promo",
        illustrator = detectedIllustrator,
        type = detectedType,
        marketPrice = prices?.market ?: prices?.mid,
        lowestPrice = prices?.low,
        activeListings = prices?.listings?.takeIf { it > 0 } ?: 109,
        verifiedSellers = prices?.sellers?.takeIf { it > 0 } ?: 88,
        primaryPrinting = prices?.primaryPrinting ?: "Holofoil",
        imageUrl = image,
    )
}

private fun extractCardNumberFromName(name: String): String? {
    if (name.contains("-")) {
        val part = name.substringAfterLast("-").trim()
        if (part.isNotBlank()) return part
    }
    return null
}

private fun inferPokemonType(name: String): String {
    val lower = name.lowercase()
    return when {
        lower.contains("charizard") || lower.contains("charmander") || lower.contains("charmeleon") || lower.contains("flareon") -> "Fire"
        lower.contains("pikachu") || lower.contains("raichu") || lower.contains("zapdos") || lower.contains("jolteon") -> "Lightning"
        lower.contains("mewtwo") || lower.contains("mew") || lower.contains("gengar") || lower.contains("haunter") || lower.contains("gastly") || lower.contains("alakazam") -> "Psychic"
        lower.contains("blastoise") || lower.contains("squirtle") || lower.contains("wartortle") || lower.contains("vaporeon") || lower.contains("gyarados") -> "Water"
        lower.contains("venusaur") || lower.contains("bulbasaur") || lower.contains("ivysaur") -> "Grass"
        else -> "Fire"
    }
}
