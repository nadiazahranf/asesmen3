package org.d3if3064.epiceat.model

data class Kuliner(
    val kuliner_id: Int,
    val user_email: String,
    val nama_makanan: String,
    val lokasi: String,
    val deskripsi: String,
    val image_id: String,
    val delete_hash: String,
    val created_at: String
)

data class KulinerCreate(
    val user_email: String,
    val nama_makanan: String,
    val lokasi: String,
    val deskripsi: String,
    val image_id: String,
    val delete_hash: String
)
