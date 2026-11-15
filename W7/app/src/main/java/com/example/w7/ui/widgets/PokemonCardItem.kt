package com.example.w7.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.w7.model.PokemonCard
import com.example.w7.ui.theme.CardBorder
import com.example.w7.ui.theme.DarkSurface
import com.example.w7.ui.theme.PriceGreen
import com.example.w7.ui.theme.PriceGreenBg
import com.example.w7.ui.theme.TextPrimary
import com.example.w7.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun PokemonCardItem(
    card: PokemonCard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, CardBorder),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
        ) {

            RemoteImage(
                url = card.imageUrl,
                contentDescription = card.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.72f)
                    .clip(RoundedCornerShape(10.dp)),
            )

            Spacer(Modifier.height(10.dp))


            Text(
                text = card.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.height(2.dp))


            val setSubtext = buildString {
                append(card.setName)
                if (card.cardNumber.isNotBlank()) {
                    append(" · #")
                    append(card.cardNumber)
                }
            }
            Text(
                text = setSubtext,
                fontSize = 11.sp,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.height(10.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                val priceText = card.marketPrice?.let {
                    String.format(Locale.US, "~ $%.2f", it)
                } ?: (card.lowestPrice?.let {
                    String.format(Locale.US, "~ $%.2f", it)
                } ?: "N/A")

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(PriceGreenBg)
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                ) {
                    Text(
                        text = priceText,
                        color = PriceGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }


                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF243045))
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                ) {
                    Text(
                        text = card.rarity,
                        color = TextSecondary,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}
