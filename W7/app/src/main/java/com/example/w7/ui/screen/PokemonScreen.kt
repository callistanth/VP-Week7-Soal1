package com.example.w7.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.w7.ui.theme.DarkBg
import com.example.w7.ui.theme.DarkSurface
import com.example.w7.ui.theme.DarkSurfaceVariant
import com.example.w7.ui.theme.LiveGreen
import com.example.w7.ui.theme.LiveGreenBg
import com.example.w7.ui.theme.TextMuted
import com.example.w7.ui.theme.TextPrimary
import com.example.w7.ui.theme.TextSecondary
import com.example.w7.ui.widgets.ConnectionFailedView
import com.example.w7.ui.widgets.LoadingBox
import com.example.w7.ui.widgets.PokeballIcon
import com.example.w7.ui.widgets.PokemonCardItem
import com.example.w7.viewmodel.PokemonScreenState
import com.example.w7.viewmodel.PokemonUiState
import com.example.w7.viewmodel.PokemonViewModel

@Composable
fun PokemonScreen(
    onCardClick: (String) -> Unit,
    viewModel: PokemonViewModel = viewModel(factory = PokemonViewModel.Factory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PokemonContent(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onSearch = viewModel::onSearchSubmitted,
        onClearQuery = viewModel::onClearQuery,
        onChipSelected = viewModel::onChipSelected,
        onCardClick = onCardClick,
        onRetry = viewModel::retry,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonContent(
    state: PokemonScreenState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClearQuery: () -> Unit,
    onChipSelected: (String) -> Unit,
    onCardClick: (String) -> Unit,
    onRetry: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val chips = listOf("Charizard", "Pikachu", "Mewtwo", "Gengar")

    Scaffold(
        containerColor = DarkBg,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            Spacer(Modifier.height(12.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    PokeballIcon()
                    Column {
                        Text(
                            text = "PokéTracker",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                        )
                        Text(
                            text = "Real-time TCG Card Valuation",
                            fontSize = 11.sp,
                            color = TextSecondary,
                        )
                    }
                }


                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(LiveGreenBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(LiveGreen),
                    )
                    Text(
                        text = "LIVE API",
                        color = LiveGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(Modifier.height(16.dp))


            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Search Pokémon...", color = TextMuted, fontSize = 14.sp)
                },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp),
                    )
                },
                trailingIcon = {
                    if (state.query.isNotEmpty()) {
                        IconButton(onClick = onClearQuery) {
                            Icon(
                                Icons.Filled.Clear,
                                contentDescription = "Clear",
                                tint = TextSecondary,
                                modifier = Modifier.size(18.dp),
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkSurface,
                    unfocusedContainerColor = DarkSurface,
                    focusedBorderColor = Color(0xFF334155),
                    unfocusedBorderColor = Color(0xFF1E293B),
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        onSearch()
                    }
                ),
            )

            Spacer(Modifier.height(12.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                chips.forEach { chip ->
                    val isSelected = state.selectedChip.equals(chip, ignoreCase = true)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            focusManager.clearFocus()
                            onChipSelected(chip)
                        },
                        label = {
                            Text(
                                text = chip,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            )
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = DarkSurface,
                            labelColor = TextSecondary,
                            selectedContainerColor = DarkSurfaceVariant,
                            selectedLabelColor = TextPrimary,
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isSelected) Color(0xFF475569) else Color(0xFF1E293B),
                            selectedBorderColor = Color(0xFF64748B),
                            enabled = true,
                            selected = isSelected,
                        ),
                    )
                }
            }

            Spacer(Modifier.height(14.dp))


            when (val uiState = state.uiState) {
                PokemonUiState.Loading -> {
                    LoadingBox(Modifier.weight(1f))
                }

                is PokemonUiState.Error -> {
                    ConnectionFailedView(
                        errorCode = uiState.message,
                        onRetry = onRetry,
                        modifier = Modifier.weight(1f),
                    )
                }

                is PokemonUiState.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {

                        item(span = { GridItemSpan(2) }) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Market Results (${uiState.cards.size})",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                )
                                Text(
                                    text = "USD · Near Mint",
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                )
                            }
                        }


                        items(uiState.cards, key = { it.id }) { card ->
                            PokemonCardItem(
                                card = card,
                                onClick = { onCardClick(card.id) },
                            )
                        }

                        if (uiState.cards.isEmpty()) {
                            item(span = { GridItemSpan(2) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(40.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text("No cards found", color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
