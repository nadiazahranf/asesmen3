package org.d3if3064.epiceat.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiFoodBeverage
import androidx.compose.material.icons.filled.RiceBowl
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.d3if3064.epiceat.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryComponent(
    modifier: Modifier = Modifier,
    categoryList: List<CategoryData>,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        categoryList.forEach {
            OutlinedCard(
                modifier = Modifier.weight(1f),
                onClick = onClick,
                content = {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(imageVector = it.icon, contentDescription = it.name)
                        Text(text = it.name, fontWeight = FontWeight.Bold)
                    }
                },
                colors = CardDefaults.outlinedCardColors(
                    containerColor = White,
                    contentColor = Color.Black
                )
            )
        }
    }
}

@Preview
@Composable
private fun CategoryComponentPrev() {
    CategoryComponent(
        categoryList = listOf(
            CategoryData(
                icon = Icons.Default.RiceBowl,
                name = "Meal"
            ),
            CategoryData(
                icon = Icons.Default.EmojiFoodBeverage,
                name = "Beverages"
            ),
            CategoryData(
                icon = Icons.Default.SoupKitchen,
                name = "Soup"
            ),
        ),
        onClick = {

        }
    )
}