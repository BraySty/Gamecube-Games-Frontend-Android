package com.example.cliente

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.cliente.api.UsuarioAPI
import com.example.cliente.interfaces.UsuarioAPIInterface
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*

class MainActivity : AppCompatActivity(), CustomDialogSignUp.ExampleDialogListener {

    private lateinit var textinput_email : TextInputEditText
    private lateinit var textinput_password : TextInputEditText
    private lateinit var check_password : MaterialCheckBox
    private lateinit var button_login : MaterialButton
    private lateinit var text_forgotpasword : MaterialTextView
    private lateinit var text_signup : MaterialTextView
    private lateinit var sendIntent : Intent

    private lateinit var button_settings : MaterialButton

    private lateinit var url:String
    private var toogle:String = "false"
    private var usuario:String = ""
    private var contrasena:String = ""
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textinput_email = findViewById(R.id.textinput_email)
        textinput_password = findViewById(R.id.textinput_password)
        check_password = findViewById(R.id.check_password)
        button_login = findViewById(R.id.button_login)
        text_forgotpasword = findViewById(R.id.text_forgotpasword)
        text_signup = findViewById(R.id.text_signup)
        button_settings = findViewById(R.id.button_server)

        context = this
        leerFicherosAjustes()
        leerFicherosCuenta()

        if (toogle == "true") {
            textinput_email.setText(usuario, TextView.BufferType.EDITABLE)
            textinput_password.setText(contrasena, TextView.BufferType.EDITABLE)
            check_password.isChecked = true
        } else {
            textinput_email.setText("", TextView.BufferType.EDITABLE)
            textinput_password.setText("", TextView.BufferType.EDITABLE)
        }

        check_password.setOnClickListener {
            toogle = check_password.isChecked.toString()
            println(toogle)
        }

        buttonsActions()//Acciones que hacaen los botones.
        textActions()//Acciones que realizan los textos.
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
        } else {
            url = "http://192.168.1.1:8080/"
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

    private fun createUser(email: String?, password: String?) {
        var rutaJUsuarios = url + "users/"
        var retrofit = Retrofit.Builder().baseUrl(rutaJUsuarios).addConverterFactory(GsonConverterFactory.create()).build()
        var interfaz: UsuarioAPIInterface = retrofit.create(UsuarioAPIInterface::class.java)
        if (email != null) {
            interfaz.crearUsuario(UsuarioAPI(email.trim(), password)).enqueue(object:
                Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    println(response.message().toString())
                    if (response.code() == 200) {
                        Toast.makeText(context, "Se ha creado la cuenta exitosamente.",Toast.LENGTH_SHORT).show()
                    } else if (response.code() == 409) {
                        Toast.makeText(context, "Este correo ya esta en uso.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message.toString()}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun readUser() {
        var email = textinput_email.text.toString()
        var password = textinput_password.text.toString()
        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()
            && (url.startsWith("http") || url.startsWith("https"))
            && url.endsWith("/")) {
            var rutaJUsuarios = url + "users/"
            var retrofit = Retrofit.Builder().baseUrl(rutaJUsuarios).addConverterFactory(GsonConverterFactory.create()).build()
            var interfaz: UsuarioAPIInterface = retrofit.create(UsuarioAPIInterface::class.java)
            if (email != null) {
                interfaz.obtenerUsuario(email).enqueue(object:Callback<UsuarioAPI> {
                    override fun onResponse(call: Call<UsuarioAPI>, response: Response<UsuarioAPI>) {
                        println(response.message().toString())
                        if (response.code() == 200) {
                            val cuerpo = response.body()
                            var cuerpoPassword = cuerpo?.password.toString()
                            if (password == cuerpoPassword) {
                                usuario = email
                                contrasena = password
                                guardarFicherosCuenta()
                                Toast.makeText(context, "Credenciales aceptadas.",Toast.LENGTH_SHORT).show()
                                sendIntent = Intent(applicationContext, principal::class.java)
                                startActivity(sendIntent)
                            } else {
                                Toast.makeText(context, "La contraseña no es correcta.",Toast.LENGTH_SHORT).show()
                            }
                        } else if (response.code() == 404) {
                            Toast.makeText(context, "Este correo no se encuentra en la base de datos.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<UsuarioAPI>, t: Throwable) {
                        Toast.makeText(context, "Error: ${t.message.toString()}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        } else {
            Toast.makeText(context, "Uno de los campos esta mal o la ruta de conexion esta mal.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun lostPassword(email: String?) {
        var rutaJUsuarios = url + "users/"
        var retrofit = Retrofit.Builder().baseUrl(rutaJUsuarios).addConverterFactory(GsonConverterFactory.create()).build()
        var interfaz: UsuarioAPIInterface = retrofit.create(UsuarioAPIInterface::class.java)
        if (email != null) {
            interfaz.recoverPassword(email).enqueue(object:
                Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    println(response.message().toString())
                    if (response.code() == 200) {
                        Toast.makeText(context, "Se ha enviado un correo a su cuenta con sus credenciales.",Toast.LENGTH_SHORT).show()
                    } else if (response.code() == 404) {
                        Toast.makeText(context, "Este correo no se encuentra en la base de datos.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message.toString()}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun textActions() {
        text_forgotpasword.setOnClickListener {
            //Crear un "Dialog" para introducir un correo para recuperar la contraseña de la cuenta

            val alert: AlertDialog.Builder = AlertDialog.Builder(this)
            alert.setTitle("¿Has olvidado tu contraseña?")
            alert.setMessage("Escribe tu correo electrónico.\n" +
                    "Esta funcion solo funciona si tiene conexion con el servidor.");
            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT

            alert.setView(input)
            alert.setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    lostPassword(input.text.toString())
                })
            alert.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, whichButton ->

                })
            alert.show()
        }

        text_signup.setOnClickListener {
            //Crear un diálogo para untroducir un nuevo correo y su contraseña.

            val dialog = CustomDialogSignUp()
            dialog.show(supportFragmentManager, "Registrar")
        }
    }

    private fun buttonsActions() {
        button_login.setOnClickListener {
            //Ir a la actividad principal.
            readUser()
        }

        button_settings.setOnClickListener {
            //Diálogo para cambiar de servidor (dirección del servidor).
            val alert: AlertDialog.Builder = AlertDialog.Builder(this)
            alert.setTitle("Ajustes de serviodr")
            alert.setMessage("Escribe una dirección de servidor.");
            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT
            input.setText(url, TextView.BufferType.EDITABLE)
            alert.setView(input)
            alert.setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    url = input.text.toString().replace(" ", "")
                    guardarFicherosAjustes()
                })
            alert.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, whichButton ->
                })
            alert.show()
        }
    }

    override fun applyTexts(email: String?, password: String?) {
        createUser(email, password)
        textinput_email.setText(email, TextView.BufferType.EDITABLE)
        textinput_password.setText(password, TextView.BufferType.EDITABLE)
        guardarFicherosCuenta()
    }
}