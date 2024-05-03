package com.example.cliente.api

import com.google.gson.annotations.SerializedName


data class ImagenAPI (

  @SerializedName("id"          ) var id          :Int? = null,
  @SerializedName("title"       ) var title       :String? = null,
  @SerializedName("developer"   ) var developer   :String? = null,
  @SerializedName("publisher"   ) var publisher   :String? = null,
  @SerializedName("europePal"   ) var europePal   :String? = null,
  @SerializedName("japan"       ) var japan       :String? = null,
  @SerializedName("northAmerica") var northAmerica:String? = null,
  @SerializedName("concatFields") var concatFields:String? = null

)