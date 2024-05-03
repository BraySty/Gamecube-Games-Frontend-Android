package com.example.cliente.interfaces

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ImagenAPIInterface {

    @GET("images/{fileName}")
    fun obtenerImagen(@Path("fileName") fileName:String): Call<ResponseBody>

}