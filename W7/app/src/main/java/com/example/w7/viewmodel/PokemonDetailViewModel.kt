package com.example.w7.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.w7.data.remote.ServiceLocator
import com.example.w7.data.repository.PokemonRepository
import com.example.w7.model.PokemonCard
import com.example.w7.navigation.PokemonDetailRoute
import com.example.w7.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface PokemonDetailUiState {
    data object Loading : PokemonDetailUiState
    data class Success(val card: PokemonCard) : PokemonDetailUiState
    data class Error(val message: String) : PokemonDetailUiState
}

class PokemonDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: PokemonRepository,
) : ViewModel() {

    private val cardId: String = savedStateHandle.toRoute<PokemonDetailRoute>().cardId
    private val _uiState = MutableStateFlow<PokemonDetailUiState>(PokemonDetailUiState.Loading)
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()
    init {
        loadCard()
    }

    fun loadCard() {
        _uiState.value = PokemonDetailUiState.Loading
        viewModelScope.launch {
            repository.getCardDetail(cardId)
                .onSuccess { card ->
                    _uiState.update { PokemonDetailUiState.Success(card) }
                }
                .onFailure { throwable ->
                    _uiState.update { PokemonDetailUiState.Error(throwable.toUserMessage()) }
                }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                PokemonDetailViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    repository = ServiceLocator.pokemonRepository,
                )
            }
        }
    }
}
