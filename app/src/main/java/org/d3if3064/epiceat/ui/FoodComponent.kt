package org.d3if3064.epiceat.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.d3if3064.epiceat.R
import org.d3if3064.epiceat.model.Kuliner
import org.d3if3064.epiceat.network.ImageApi
import org.d3if3064.epiceat.ui.theme.PinkPink
import org.d3if3064.epiceat.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodComponent(
    modifier: Modifier = Modifier,
    color: Color,
    kuliner: Kuliner,
    onCardClick: () -> Unit,
    onDeleteClick: () -> Unit
//    onFavouriteClick: () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color,
            contentColor = White
        ),
        onClick = onCardClick
    ) {
        Row {
            Row(
                modifier = Modifier.padding(8.dp),
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(ImageApi.getImageUrl(kuliner.image_id))
                        .crossfade(true)
                        .build(),
                    contentDescription = "Gambar ${kuliner.image_id}",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.broken_image),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(15.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.weight(3f),
                ) {
                    Row {
                        Column(verticalArrangement = Arrangement.SpaceAround) {
                            Text(
                                text = kuliner.nama_makanan,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                fontWeight = FontWeight.Light,
                                text = kuliner.lokasi, maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = kuliner.deskripsi,
                                fontSize = 14.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        IconButton(onClick = { onDeleteClick() }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete Icon",
                                tint = Color.White
                            )
                        }
                    }

                }
//            IconButton(
//                onClick = onFavouriteClick,
//                content = {
//                    Icon(
//                        imageVector = Icons.Default.FavoriteBorder,
//                        contentDescription = "Favourite",
//                        tint = PinkPink
//                    )
//                },
//                modifier = Modifier.align(alignment = CenterVertically)
//            )

            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

            }
        }
    }
}

@Preview
@Composable
private fun FoodComponentPrev() {
//    FoodComponent(
//        color = Color(0xFF307E54),
//        image = R.drawable.keraktelor,
//        foodName = "Kerak Telor",
//        foodOrigin = "Jakarta",
//        foodDesc = "LoremIpsumLoremIpsumLoremIpsumLoremIpsumLoremIpsumLoremIpsumLoremIpsum",
//        onCardClick = { /*TODO*/ },
////        onFavouriteClick = { /*TODO*/ }
//    )
}