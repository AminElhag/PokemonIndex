package com.example.pokemonindex.data.remote

import com.example.pokemonindex.common.Resource
import com.example.pokemonindex.data.remote.model.PokemonInfo
import com.example.pokemonindex.data.remote.model.PokemonList
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(
    private val api: PokemonApi
) {
    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        val response = try {
            api.pokemonList(limit, offset)
        } catch (e: Exception) {
            return Resource.Error(e.message)
        }
        return Resource.Success(response)
    }

    suspend fun getPokemonInfo(pokemonName: String): Resource<PokemonInfo> {
        val response = try {
            api.pokemonInfo(pokemonName)
        } catch (e: Exception) {
            return Resource.Error(e.message)
        }
        return Resource.Success(response)
    }
}