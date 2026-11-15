package com.example.w7.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.w7.data.remote.ServiceLocator
import com.example.w7.data.repository.PokemonRepository
import com.example.w7.model.PokemonCard
import com.example.w7.util.toUserMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface PokemonUiState {
    data object Loading : PokemonUiState
    data class Success(val cards: List<PokemonCard>) : PokemonUiState
    data class Error(val message: String) : PokemonUiState
}

data class PokemonScreenState(
    val query: String = "Charizard",
    val selectedChip: String = "Charizard",
    val uiState: PokemonUiState = PokemonUiState.Loading,
)

class PokemonViewModel(
    private val repository: PokemonRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(PokemonScreenState())
    val state: StateFlow<PokemonScreenState> = _state.asStateFlow()
    private var searchJob: Job? = null
    init {
        search("Charizard")
    }

    fun onQueryChange(newQuery: String) {
        _state.update { it.copy(query = newQuery) }
    }

    fun onSearchSubmitted() {
        val q = _state.value.query.trim()
        if (q.isNotEmpty()) {
            search(q)
        }
    }

    fun onChipSelected(chip: String) {
        _state.update { it.copy(query = chip, selectedChip = chip) }
        search(chip)
    }

    fun onClearQuery() {
        _state.update { it.copy(query = "") }
    }

    fun retry() {
        val q = _state.value.query.ifBlank { "Charizard" }
        search(q)
    }

    private fun search(query: String) {
        searchJob?.cancel()
        _state.update { it.copy(uiState = PokemonUiState.Loading) }
        searchJob = viewModelScope.launch {
            repository.searchCards(query)
                .onSuccess { cards ->
                    _state.update { it.copy(uiState = PokemonUiState.Success(cards)) }
                }
                .onFailure { throwable ->
                    _state.update { it.copy(uiState = PokemonUiState.Error(throwable.toUserMessage())) }
                }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { PokemonViewModel(ServiceLocator.pokemonRepository) }
        }
    }
}
