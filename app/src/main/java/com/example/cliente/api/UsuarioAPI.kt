package com.example.cliente.api

import com.google.gson.annotations.SerializedName

data class UsuarioAPI (
  @SerializedName("correo"  ) var correo  :String? = null,
  @SerializedName("password") var password:String? = null,
)