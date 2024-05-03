package com.example.cliente

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.cliente.api.UsuarioAPI
import com.example.cliente.interfaces.UsuarioAPIInterface
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*

class Account : AppCompatActivity() {

    private lateinit var email_password : TextInputEditText
    private lateinit var actualizar_contrasena : MaterialButton
    private lateinit var borrar_cuenta : MaterialButton

    private lateinit var context: Context
    private lateinit var url:String
    private var toogle:String = "false"
    private var usuario:String = ""
    private var contrasena:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        var emailaccount = findViewById<MaterialTextView>(R.id.email_account)
        email_password  =findViewById(R.id.email_password)
        actualizar_contrasena  = findViewById(R.id.actualizar_contrasena)
        borrar_cuenta  =findViewById(R.id.borrar_cuenta)

        context = this
        leerFicherosAjustes()
        leerFicherosCuenta()

        emailaccount.setText(usuario, TextView.BufferType.EDITABLE)
        email_password.setText(contrasena, TextView.BufferType.EDITABLE)

        actualizar_contrasena.setOnClickListener {
            contrasena = email_password.text.toString()
            actualizarUser()
            guardarFicherosCuenta()
        }

        borrar_cuenta.setOnClickListener{
            //Diálogo para cambiar de servidor (dirección del servidor).
            val alert: AlertDialog.Builder = AlertDialog.Builder(this)
            alert.setTitle("Eliminar cuenta")
            alert.setMessage("Al terminar la operacion la aplicacion se cerrara automaticamente.\n" +
                    "¿Esta seguro que desea continuar?");
            alert.setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    deleteUser()
                    Toast.makeText(context, "Adios...", Toast.LENGTH_SHORT).show()
                })
            alert.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, whichButton ->
                })
            alert.show()
        }
    }

    private fun leerFicherosCuenta() {
        val rutaAplicacion = applicationContext.filesDir.toString()
        val filename = "data.dat"
        val ruta = File(rutaAplicacion)
        val fichero = File(ruta, filename)
        println("--------------------------------------------")
        if (fichero.exists()) {
            val bufferedReader = BufferedReader(FileReader(fichero))
            toogle = bufferedReader.readLine()
            usuario = bufferedReader.readLine()
            contrasena = bufferedReader.readLine()
        } else {
            toogle = "false"
            usuario = ""
            contrasena = ""
        }
    }

    private fun guardarFicherosCuenta() {
        val rutaAplicacion = applicationContext.filesDir.toString()
        val filename = "data.dat"
        println("--------------------------------------------")
        println(applicationContext.filesDir.toString())
        println("|$toogle|")
        println("|$contrasena|")
        println("|$contrasena|")
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
            output.write(toogle.toByteArray())
            output.write("\n".toByteArray())
            output.write(usuario.toByteArray())
            output.write("\n".toByteArray())
            output.write(contrasena.toByteArray())
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
            url = fileContents
        } else {
            url = "http://192.168.1.1:8080/"
        }
    }

    private fun actualizarUser() {
        var rutaJUsuarios = url + "users/"
        var retrofit = Retrofit.Builder().baseUrl(rutaJUsuarios).addConverterFactory(
            GsonConverterFactory.create()).build()
        var interfaz: UsuarioAPIInterface = retrofit.create(UsuarioAPIInterface::class.java)
        if (usuario != null) {
            interfaz.actualizarUsuario(usuario, UsuarioAPI(usuario,contrasena)).enqueue(object:
                Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    println(response.message().toString())
                    if (response.code() == 200) {
                        Toast.makeText(context, "Se ha actualizado la contraseña.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message.toString()}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun deleteUser() {
        var rutaJUsuarios = url + "users/"
        var retrofit = Retrofit.Builder().baseUrl(rutaJUsuarios).addConverterFactory(
            GsonConverterFactory.create()).build()
        var interfaz: UsuarioAPIInterface = retrofit.create(UsuarioAPIInterface::class.java)
        if (usuario != null) {
            interfaz.borrarUsuario(usuario).enqueue(object:
                Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    println(response.message().toString())
                    if (response.code() == 200) {
                        Toast.makeText(context, "Usuario elimnado.",
                            Toast.LENGTH_SHORT).show()
                        finishAffinity()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message.toString()}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}