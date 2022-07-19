package com.example.pokemonindex.data.remote

import com.example.pokemonindex.data.remote.model.PokemonInfo
import com.example.pokemonindex.data.remote.model.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon")
    suspend fun pokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): PokemonList

    @GET("pokemon/{name}")
    suspend fun pokemonInfo(
        @Path("name") pokemonName: String
    ): PokemonInfo
}