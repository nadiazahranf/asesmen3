package org.d3if3064.epiceat.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.d3if3064.epiceat.R
import org.d3if3064.epiceat.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityComponent(
    modifier: Modifier = Modifier,
    color: Color,
    image: Int,
    text: String,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            contentColor = White,
            containerColor = color
        ),
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = text,
                modifier = Modifier.weight(1f).aspectRatio(1f),
                contentScale = ContentScale.Crop    ,
            )
            Text(
                text = text,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Preview
@Composable
private fun CityComponentPrev() {
    CityComponent(
        color = Color(0xFF1565C0),
        image = R.drawable.monas,
        text = "Jakarta",
        onClick = { /*TODO*/ }
    )
}