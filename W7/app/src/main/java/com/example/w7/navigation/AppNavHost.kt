package com.example.w7.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.w7.ui.screen.PokemonDetailScreen
import com.example.w7.ui.screen.PokemonScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PokemonListRoute,
    ) {
        composable<PokemonListRoute> {
            PokemonScreen(
                onCardClick = { cardId ->
                    navController.navigate(PokemonDetailRoute(cardId))
                },
            )
        }

        composable<PokemonDetailRoute> {
            PokemonDetailScreen(
                onBackClick = {
                    navController.navigateUp()
                },
            )
        }
    }
}
