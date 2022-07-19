package com.example.pokemonindex.pokemonlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.pokemonindex.common.Constant.PAGE_SIZE
import com.example.pokemonindex.common.Resource
import com.example.pokemonindex.data.remote.PokemonRepository
import com.example.pokemonindex.model.PokemonListEnter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var curPage = 0

    var pokemonList = mutableStateOf<List<PokemonListEnter>>(listOf())
    var loadingError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cashPokemonList = listOf<PokemonListEnter>()
    private var isSearchStarting = true
    val isSearching = mutableStateOf(false)

    init {
        loadPokemonPaginated()
    }

    fun searchForPokemon(query: String) {
        val searchList = if (isSearchStarting) {
            pokemonList.value
        } else {
            cashPokemonList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                pokemonList.value = cashPokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val result = searchList.filter {
                it.pokemonName.contains(query.trim(), true) ||
                        it.pokemonNumber.toString() == query.trim()
            }
            if (isSearchStarting) {
                cashPokemonList = pokemonList.value
                isSearchStarting = false
            }
            pokemonList.value = result
            isSearching.value = true
        }
    }

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            when (val result = repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)) {
                is Resource.Success -> {
                    endReached.value = curPage * PAGE_SIZE >= result.data!!.count
                    val pokemonEnter = result.data.results.mapIndexed { _, result ->
                        val number = if (result.url.endsWith("/")) {
                            result.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            result.url.dropLast(1).takeLastWhile { it.isDigit() }
                        }
                        val imageUrl =
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$number.png"
                        PokemonListEnter(result.name, imageUrl, number.toInt())
                    }
                    curPage++
                    loadingError.value = ""
                    isLoading.value = false
                    pokemonList.value += pokemonEnter
                }
                is Resource.Error -> {
                    loadingError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }

    fun calcDominantColor(drawable: Drawable, onFinished: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinished(Color(colorValue))
            }
        }
    }


}