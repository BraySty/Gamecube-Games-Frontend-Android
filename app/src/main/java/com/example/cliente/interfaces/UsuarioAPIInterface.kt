package com.example.cliente.interfaces

import com.example.cliente.api.UsuarioAPI
import retrofit2.Call
import retrofit2.http.*

interface UsuarioAPIInterface {

    @POST("usuario")
    fun crearUsuario(@Body usuario: UsuarioAPI): Call<Void>

    @POST("lostEmail/{correo}")
    fun recoverPassword(@Path("correo") correo:String): Call<Void>

    @GET("usuario/{correo}")
    fun obtenerUsuario(@Path("correo") correo:String): Call<UsuarioAPI>

    @PUT("usuario/{correo}")
    fun actualizarUsuario(@Path("correo") correo:String,
                          @Body usuario: UsuarioAPI
    ): Call<Void>

    @DELETE("usuario/{correo}")
    fun borrarUsuario(@Path("correo") correo:String): Call<Void>
}