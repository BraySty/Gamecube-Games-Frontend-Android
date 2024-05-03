package com.example.cliente

data class Videogame(
    var id : Int?,
    var title : String?,
    var developer : String?,
    var publisher : String?,
    var europe : String?,
    var japan : String?,
    var america : String?,
    var urlImage : String? = null
) {
}