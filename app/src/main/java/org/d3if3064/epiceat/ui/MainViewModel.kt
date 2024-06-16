package org.d3if3064.epiceat.ui

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.d3if3064.epiceat.model.Kuliner
import org.d3if3064.epiceat.model.KulinerCreate
import org.d3if3064.epiceat.network.Api
import org.d3if3064.epiceat.network.ApiStatus
import java.io.ByteArrayOutputStream

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Kuliner>())
        private set

    var status = MutableStateFlow(ApiStatus.FAILED)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    var querySuccess = mutableStateOf(false)
        private set


    fun retrieveData(userId: String) {
        Log.d("MainVM", "${status.value}")
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = Api.userService.getAllData(userId)
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(email: String, nama: String, lokasi: String, deskripsi: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val upload = org.d3if3064.epiceat.network.ImageApi.imgService.uploadImg(
                    image = bitmap.toMultipartBody()
                )

                if (upload.success) {
                    Api.userService.addData(
                        KulinerCreate(
                            email,
                            nama,
                            lokasi,
                            deskripsi,
                            transformImageData(upload.data),
                            upload.data.deletehash!!
                        )
                    )
                    retrieveData(email)
                    status.value = ApiStatus.SUCCESS
                    querySuccess.value = true
                }
            } catch (e: Exception) {
                status.value = ApiStatus.FAILED
                Log.d("MainVM", "${e.message}")
                if (e.message == "HTTP 500 ") {
                    errorMessage.value = "Error: Database Idle, Please try again."
                } else {
                    errorMessage.value = "Error: ${e.message}"
                    Log.d("MainViewModel", "Failure: ${e.message}")
                }
            }
        }
    }


    fun deleteData(email: String, id: Int, deleteHash: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val upload = org.d3if3064.epiceat.network.ImageApi.imgService.deleteImg(
                    deleteHash = deleteHash
                )
                if (upload.success) {
                    Api.userService.deleteData(id, email)
                    querySuccess.value = true
                    retrieveData(email)
                }
            } catch (e: Exception) {
                if (e.message == "HTTP 500 ") {
                    errorMessage.value = "Error: Database Idle, harap eksekusi data kembali."
                } else {
                    errorMessage.value = "Error: ${e.message}"
                    Log.d("MainViewModel", "Failure: ${e.message}")
                }
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size
        )
        return MultipartBody.Part.createFormData("image", "image.jpg", requestBody)
    }

    fun transformImageData(imageData: org.d3if3064.epiceat.model.ImageData): String {
        val extension = when (imageData.type) {
            "image/png" -> "png"
            "image/jpeg" -> "jpg"
            "image/gif" -> "gif"
            else -> throw IllegalArgumentException("Unsupported image type")
        }
        return "${imageData.id}.$extension"
    }

    fun clearMessage() {
        errorMessage.value = null
        querySuccess.value = false
    }

}