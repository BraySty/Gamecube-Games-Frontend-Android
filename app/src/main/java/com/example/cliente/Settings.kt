package com.example.cliente

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.io.*

class Settings : AppCompatActivity() {

    private lateinit var direccion_servidor_value : TextInputEditText
    private lateinit var actualizar_servidor : MaterialButton

    private lateinit var url:String
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        direccion_servidor_value = findViewById(R.id.direccion_servidor_value)
        actualizar_servidor = findViewById(R.id.actualizar_contrasena)
        context = this
        leerFicherosAjustes()
        direccion_servidor_value.setText(url, TextView.BufferType.EDITABLE)

        actualizar_servidor.setOnClickListener {
            if ((url.startsWith("http") || url.startsWith("https"))
                && url.endsWith("/")) {
                url = direccion_servidor_value.text.toString()
                guardarFicherosAjustes()
                Toast.makeText(context, "La URL se ha actualizado a\n $url", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun leerFicherosAjustes() {
        val rutaAplicacion = applicationContext.filesDir.toString()
        val filename = "Conection.txt"
        val ruta = File(rutaAplicacion)
        val fichero = File(ruta, filename)
        println("--------------------------------------------")
        if (fichero.exists()) {
            val bufferedReader = BufferedReader(FileReader(fichero))
            val stringBuilder = StringBuilder()
            bufferedReader.useLines { lines ->
                lines.forEach {
                    stringBuilder.append(it)
                }
            }
            val fileContents = stringBuilder.toString()
            println(fileContents)
            url = fileContents.replace(" ", "")
            println("|$url|")
        } else {
            url = "http://192.168.0.0:8080"
            println("|$url|")
        }
    }

    private fun guardarFicherosAjustes() {
        val rutaAplicacion = applicationContext.filesDir.toString()
        val filename = "Conection.txt"
        println("--------------------------------------------")
        println(applicationContext.filesDir.toString())
        println("|$url|")
        val ruta = File(rutaAplicacion)
        if (!ruta.exists()) {
            ruta.mkdirs()
        }
        val fichero = File(ruta, filename)
        if (!fichero.exists()) {
            fichero.createNewFile()
        }
        var output = FileOutputStream(fichero)
        try {
            var outputText = url.replace(" ", "")
            output.write(outputText.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                output.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}