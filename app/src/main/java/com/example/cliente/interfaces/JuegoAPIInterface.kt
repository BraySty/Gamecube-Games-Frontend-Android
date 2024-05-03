package com.example.cliente.interfaces

import com.example.cliente.api.JuegoAPI
import retrofit2.Call
import retrofit2.http.*

interface JuegoAPIInterface {

    @POST("juegos/juego")
    fun crearJuego(@Body juego: JuegoAPI): Call<Void>

    @GET("juegos/juego")
    fun obtenerJuego(): Call<JuegoAPI>

    @GET("juegos/juego")
    fun buscarJuego(@Path("title") title:String,
                     @Path("developer") developer:String): Call<List<JuegoAPI>>

    @GET("juegos")
    fun obtenerJuegos(): Call<List<JuegoAPI>>

    @GET("juegos")
    fun buscarJuegos(@Path("title") title:String,
                     @Path("developer") developer:String): Call<List<JuegoAPI>>

    @PUT("juegos/juego/{id}")
    fun actualizarJuego(@Path("id") id:Int,
                        @Body juego: JuegoAPI
    ): Call<Void>

    @DELETE("juegos/juego/{id}")
    fun borrarJuego(@Path("id") id:Int): Call<Void>
}