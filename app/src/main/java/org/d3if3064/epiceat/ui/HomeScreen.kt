package org.d3if3064.epiceat.ui

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiFoodBeverage
import androidx.compose.material.icons.filled.RiceBowl
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3064.epiceat.BuildConfig
import org.d3if3064.epiceat.R
import org.d3if3064.epiceat.model.Kuliner
import org.d3if3064.epiceat.network.ApiStatus
import org.d3if3064.epiceat.network.UserDataStore
import org.d3if3064.epiceat.ui.theme.Pink80
import org.d3if3064.epiceat.ui.theme.PinkPink
import org.d3if3064.epiceat.ui.theme.Purple40
import org.d3if3064.epiceat.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()
    val dataStore = UserDataStore(context)
    val errorMessage by viewModel.errorMessage
    val user by dataStore.userFlow.collectAsState(org.d3if3064.epiceat.model.User())

    val data by viewModel.data
    val status by viewModel.status.collectAsState()
    var showHapusDialog by remember { mutableStateOf(false) }
    var kulinerData by remember { mutableStateOf<Kuliner?>(null) }

    LaunchedEffect(user.email) {
        viewModel.retrieveData(user.email)
    }
    val isSuccess by viewModel.querySuccess

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            Toast.makeText(context, "Berhasil!", Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }
    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            viewModel.clearMessage()
        }
    }

    var showImgDialog by remember { mutableStateOf(false) }
    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
        if (bitmap != null) showImgDialog = true
    }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        ProfilDialog(
            user = user,
            onDismissRequest = { showDialog = false }) {
            CoroutineScope(Dispatchers.IO).launch { signOut(context, dataStore) }
            showDialog = false
        }
    }
    if (showImgDialog) {
        ImageDialog(
            bitmap = bitmap,
            onDismissRequest = { showImgDialog = false }) { nama_deskripsi, lokasi, deskripsi ->
            viewModel.saveData(user.email, nama_deskripsi, lokasi, deskripsi, bitmap!!)
            showImgDialog = false

        }
    }
    if (showHapusDialog) {
        HapusDialog(
            data = kulinerData!!,
            onDismissRequest = { showHapusDialog = false }) {
            viewModel.deleteData(
                user.email,
                kulinerData!!.kuliner_id,
                kulinerData!!.delete_hash
            )
            showHapusDialog = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(8.dp, spotColor = Color.DarkGray),
                title = {
                    Column {
                        Text(
                            text = "Selamat datang, ",
                            fontSize = if (user.name != "") 16.sp else 20.sp,
                            fontWeight = if (user.name != "") FontWeight.Normal else FontWeight.Bold
                        )
                        if (user.name != "") {
                            Text(
                                text = user.name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = PinkPink,
                    titleContentColor = White,
                    actionIconContentColor = White,
                ),
                actions = {
                    IconButton(onClick = {

                        if (user.email.isEmpty()) {
                            CoroutineScope(Dispatchers.IO).launch { signIn(context, dataStore) }
                        } else {
                            showDialog = true
                        }

                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.account_circle),
                            contentDescription = "profile",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            )
        },
        containerColor = White,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                top = it.calculateTopPadding() + 16.dp,
                start = 16.dp,
                end = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
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
            item {
                SearchBar(
                    value = "",
                    onSearchAction = {

                    }
                )
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Makanan Favorit",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        onClick = {
                            if (user.email.isNotEmpty() && user.email != "") {
                                val options = CropImageContractOptions(
                                    null, CropImageOptions(
                                        imageSourceIncludeGallery = true,
                                        imageSourceIncludeCamera = true,
                                        fixAspectRatio = true
                                    )
                                )
                                launcher.launch(options)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Harap login terlebih dahulu.",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        },
                        colors = buttonColors(containerColor = Purple40, contentColor = Color.White)
                    ) {
                        Text(text = "Tambah", fontWeight = FontWeight.Normal, fontSize = 14.sp)
                    }
                }
            }
            when (status) {
                ApiStatus.SUCCESS -> {
                    items(data) {
                        FoodComponent(
                            color = Color(0xFF307E54),
                            kuliner = it,
                            onCardClick = { /*TODO*/ },
//                    onFavouriteClick = { /*TODO*/ }
                        ) {
                            kulinerData = it
                            showHapusDialog = true
                        }

                    }
                }

                ApiStatus.LOADING -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                ApiStatus.FAILED -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (user.email.isEmpty()) {
                                Text(text = "Anda belum login.")
                            } else {
                                Text(text = "Terjadi kesalahan atau data kosong.")
                                Button(
                                    onClick = { viewModel.retrieveData(user.email) },
                                    modifier = Modifier.padding(top = 16.dp),
                                    contentPadding = PaddingValues(
                                        horizontal = 32.dp,
                                        vertical = 16.dp
                                    ),
                                    colors = buttonColors(
                                        containerColor = Pink80,
                                        contentColor = White
                                    )
                                ) {
                                    Text(text = "Coba lagi")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getCroppedImage(
    resolver: ContentResolver,
    result: CropImageView.CropResult
): Bitmap? {
    if (!result.isSuccessful) {
        Log.e("IMAGE", "Error: ${result.error}")
        return null
    }
    val uri = result.uriContent ?: return null

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        MediaStore.Images.Media.getBitmap(resolver, uri)
    } else {
        val source = ImageDecoder.createSource(resolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}

private suspend fun signIn(context: Context, dataStore: UserDataStore) {
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.API_KEY)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)
        handleSignIn(result, dataStore)
    } catch (e: GetCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private suspend fun handleSignIn(result: GetCredentialResponse, dataStore: UserDataStore) {
    val credential = result.credential
    if (credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
    ) {
        try {
            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
            val nama = googleId.displayName ?: ""
            val email = googleId.id
            val photoUrl = googleId.profilePictureUri.toString()
            dataStore.saveData(org.d3if3064.epiceat.model.User(nama, email, photoUrl))
        } catch (e: GoogleIdTokenParsingException) {
            Log.e("SIGN-IN", "Error: ${e.message}")
        }
    } else {
        Log.e("SIGN-IN", "Error: unrecognized custom credential type.")
    }
}

private suspend fun signOut(context: Context, dataStore: UserDataStore) {
    try {
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        dataStore.saveData(org.d3if3064.epiceat.model.User())
    } catch (e: ClearCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

@Preview
@Composable
private fun HomeScreenPrev() {
    HomeScreen()
}