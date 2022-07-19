package com.example.pokemonindex.pokemondetail

import androidx.lifecycle.ViewModel
import com.example.pokemonindex.common.Resource
import com.example.pokemonindex.data.remote.PokemonRepository
import com.example.pokemonindex.data.remote.model.PokemonInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    suspend fun getPokemonInfo(pokemonName: String): Resource<PokemonInfo> {
        return repository.getPokemonInfo(pokemonName)
    }
}