package com.example.w7.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.w7.model.PokemonCard
import com.example.w7.ui.theme.CardBorder
import com.example.w7.ui.theme.DarkBg
import com.example.w7.ui.theme.DarkSurface
import com.example.w7.ui.theme.PokeRed
import com.example.w7.ui.theme.PriceGold
import com.example.w7.ui.theme.PriceGreen
import com.example.w7.ui.theme.TextPrimary
import com.example.w7.ui.theme.TextSecondary
import com.example.w7.ui.widgets.ConnectionFailedView
import com.example.w7.ui.widgets.LoadingBox
import com.example.w7.ui.widgets.RemoteImage
import com.example.w7.viewmodel.PokemonDetailUiState
import com.example.w7.viewmodel.PokemonDetailViewModel
import java.util.Locale

@Composable
fun PokemonDetailScreen(
    onBackClick: () -> Unit,
    viewModel: PokemonDetailViewModel = viewModel(factory = PokemonDetailViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = DarkBg,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (val state = uiState) {
                PokemonDetailUiState.Loading -> {
                    LoadingBox(Modifier.align(Alignment.Center))
                }

                is PokemonDetailUiState.Error -> {
                    ConnectionFailedView(
                        errorCode = state.message,
                        onRetry = viewModel::loadCard,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                is PokemonDetailUiState.Success -> {
                    PokemonDetailContent(
                        card = state.card,
                        onBackClick = onBackClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun PokemonDetailContent(
    card: PokemonCard,
    onBackClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF243045)),
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        Spacer(Modifier.height(8.dp))


        RemoteImage(
            url = card.imageUrl,
            contentDescription = card.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(190.dp)
                .aspectRatio(0.72f)
                .clip(RoundedCornerShape(12.dp)),
        )

        Spacer(Modifier.height(18.dp))


        Text(
            text = card.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
        )

        Spacer(Modifier.height(4.dp))


        Text(
            text = card.setName,
            fontSize = 13.sp,
            color = TextSecondary,
        )

        Spacer(Modifier.height(20.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            val marketPriceStr = card.marketPrice?.let {
                String.format(Locale.US, "$%.2f", it)
            } ?: "N/A"

            val lowPriceStr = card.lowestPrice?.let {
                String.format(Locale.US, "$%.2f", it)
            } ?: "N/A"

            PriceMetricBox(
                label = "Market Price",
                price = marketPriceStr,
                priceColor = PriceGreen,
                icon = Icons.Filled.TrendingUp,
                modifier = Modifier.weight(1f),
            )

            PriceMetricBox(
                label = "Lowest Price",
                price = lowPriceStr,
                priceColor = PriceGold,
                icon = Icons.Filled.LocalOffer,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(Modifier.height(24.dp))


        SectionTitle(title = "Market Activity")
        Spacer(Modifier.height(10.dp))

        SpecificationRow(label = "Active Listings", value = card.activeListings.toString())
        SpecificationRow(label = "Verified Sellers", value = card.verifiedSellers.toString())
        SpecificationRow(label = "Primary Printing", value = card.primaryPrinting)

        Spacer(Modifier.height(20.dp))


        SectionTitle(title = "Card Specifications")
        Spacer(Modifier.height(10.dp))

        SpecificationRow(label = "Card Number", value = card.cardNumber)
        SpecificationRow(label = "Rarity", value = card.rarity)
        SpecificationRow(label = "Type", value = card.type)
        SpecificationRow(label = "Illustrator", value = card.illustrator)

        Spacer(Modifier.height(28.dp))


        Button(
            onClick = onBackClick,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PokeRed,
                contentColor = Color.White,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            Text(
                text = "Done",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun PriceMetricBox(
    label: String,
    price: String,
    priceColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, CardBorder),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = TextSecondary,
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = price,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = priceColor,
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
        )
    }
}

@Composable
private fun SpecificationRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = TextSecondary,
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
        )
    }
}
