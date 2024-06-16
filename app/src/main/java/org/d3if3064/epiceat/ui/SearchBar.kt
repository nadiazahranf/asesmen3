package org.d3if3064.epiceat.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.d3if3064.epiceat.ui.theme.PinkPink
import org.d3if3064.epiceat.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onSearchAction: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onSearchAction,
        modifier = modifier.fillMaxSize(),
        shape = CircleShape,
        maxLines = 1,
        singleLine = true,
        placeholder = {
            Text(text = "Cari makanan..", fontSize = 14.sp)
        },
        trailingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = White,
            focusedContainerColor = White,
            unfocusedBorderColor = PinkPink,
            focusedBorderColor = PinkPink
        ),
        textStyle = TextStyle(
            fontSize = 14.sp
        ),
    )
}

@Preview
@Composable
private fun SearchBarPrev() {
    SearchBar(
        value = "",
        onSearchAction = {

        },
    )
}