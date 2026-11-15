package com.example.w7.data.remote

import com.example.w7.BuildConfig
import com.example.w7.data.repository.PokemonRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object ServiceLocator {

    const val API_KEY_FALLBACK: String = "pokeprice_free_a5a2ac1a980775ba4b498d3ad293c44375ef0aaa01da57b5" //"pokeprice_free_5ad5de8e6a3e95c69771510906ab1fc5b5bcfd64d1081487"

    fun getApiKey(): String {
        return BuildConfig.POKEMON_API_KEY.ifBlank { API_KEY_FALLBACK }
    }

    private const val BASE_URL = "https://www.pokemonpricetracker.com/"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor { getApiKey() })
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC
                else HttpLoggingInterceptor.Level.NONE
            })
            .build()
    }

    val api: PokemonApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PokemonApi::class.java)
    }

    val pokemonRepository: PokemonRepository by lazy {
        PokemonRepository(api)
    }
}
