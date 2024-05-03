package com.example.cliente

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.cliente.R
import com.squareup.picasso.Picasso

class InfoVideogame : AppCompatActivity() {

    private lateinit var id : TextView
    private lateinit var title : TextView
    private lateinit var developer : TextView
    private lateinit var publisher : TextView
    private lateinit var europe : TextView
    private lateinit var japan : TextView
    private lateinit var america : TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_videogame)

        id = findViewById(R.id.videogame_id_value)
        title = findViewById(R.id.videogame_title_value)
        developer = findViewById(R.id.videogame_developer_value)
        publisher = findViewById(R.id.videogame_publisher_value)
        europe = findViewById(R.id.videogame_europe_value)
        japan = findViewById(R.id.videogame_japan_value)
        america = findViewById(R.id.videogame_america_value)

        val imagenJuego = findViewById<ImageView>(R.id.videgame_image)

        val recibido : Intent = intent
        id.text = recibido.getIntExtra("id", 0).toString()
        title.text = recibido.getStringExtra("title")
        developer.text = recibido.getStringExtra("developer")
        publisher.text = recibido.getStringExtra("publisher")
        europe.text = recibido.getStringExtra("urlImage")
        japan.text = recibido.getStringExtra("japan")
        america.text = recibido.getStringExtra("america")
        var urlImage = recibido.getStringExtra("urlImage")
        imagenJuego.setBackgroundResource(0)
        Picasso.get().load(urlImage)
            .placeholder(R.drawable.videogames)
            .error(R.drawable.videogames)
            .into(imagenJuego)
    }
}