package org.d3if3064.epiceat.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.d3if3064.epiceat.model.MessageResponse
import org.d3if3064.epiceat.model.Kuliner
import org.d3if3064.epiceat.model.KulinerCreate
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://epic-eat.vercel.app/"


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()



private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface UserApi {
    @POST("kuliners/")
    suspend fun addData(
        @Body data: KulinerCreate
    ): MessageResponse

    @GET("kuliners/")
    suspend fun getAllData(
        @Query("email") email: String,
    ): List<Kuliner>

    @DELETE("kuliners/{kuliner_id}")
    suspend fun deleteData(
        @Path("kuliner_id") id: Int,
        @Query("email") email: String
    ): MessageResponse
}


object Api {
    val userService: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

}

enum class ApiStatus { LOADING, SUCCESS, FAILED }