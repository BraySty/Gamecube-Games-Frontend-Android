package com.example.cliente

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cliente.R
import com.example.cliente.fragments.FragmentVideogames
import com.google.android.material.textfield.TextInputEditText

class CreateVideogame : AppCompatActivity() {

    private lateinit var id : TextInputEditText
    private lateinit var title : TextInputEditText
    private lateinit var developer : TextInputEditText
    private lateinit var publisher : TextInputEditText
    private lateinit var europe : TextInputEditText
    private lateinit var japan : TextInputEditText
    private lateinit var america : TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_videogame)

        id = findViewById(R.id.videogame_input_id)
        title = findViewById(R.id.videogame_input_title)
        developer = findViewById(R.id.videogame_input_developer)
        publisher = findViewById(R.id.videogame_input_publisher)
        europe = findViewById(R.id.videogame_input_europe)
        japan = findViewById(R.id.videogame_input_japan)
        america = findViewById(R.id.videogame_input_america)
    }

    private fun onEmptyFields() {
        if(id.text.isNullOrEmpty() ) {
            id.setText("0")
        }
        if(title.text.isNullOrEmpty() ) {
            title.setText("Desconocido")
        }
        if(developer.text.isNullOrEmpty() ) {
            developer.setText("Desconocido")
        }
        if(publisher.text.isNullOrEmpty() ) {
            publisher.setText("Desconocido")
        }
        if(europe.text.isNullOrEmpty() ) {
            europe.setText("Desconocido")
        }
        if(japan.text.isNullOrEmpty() ) {
            japan.setText("Desconocido")
        }
        if(america.text.isNullOrEmpty() ) {
            america.setText("Desconocido")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        //Si el usuario no rellena algún campo, se le dan valores por defecto
        onEmptyFields()

        val devolver = Intent(applicationContext, FragmentVideogames::class.java)
        devolver.putExtra("id", Integer.parseInt(id.text.toString() ) )
        devolver.putExtra("title", title.text.toString() )
        devolver.putExtra("developer", developer.text.toString() )
        devolver.putExtra("publisher", publisher.text.toString() )
        devolver.putExtra("europe", europe.text.toString() )
        devolver.putExtra("japan", japan.text.toString() )
        devolver.putExtra("america", america.text.toString() )
        setResult(RESULT_OK, devolver)

        super.onBackPressed()
    }
}