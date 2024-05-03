package com.example.cliente.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apirest.Capture
import com.example.apirest.DeleteItem
import com.example.apirest.Update
import com.example.cliente.api.JuegoAPI
import com.example.cliente.interfaces.JuegoAPIInterface
import com.example.cliente.*
import com.example.cliente.adapter.AdapterVideogame
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentVideogames.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentVideogames : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var recyclerVideogame : RecyclerView
    private var videogameList : ArrayList<Videogame> = ArrayList()
    private lateinit var adapterVideogame : AdapterVideogame
    private lateinit var createvideogame : FloatingActionButton
    private lateinit var filtervideogames : SearchView

    private lateinit var url:String
    private var idJuego by Delegates.notNull<Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_videogames, container, false)
        leerFicherosAjustes()
        var rutaJuegos = url + "gamecube/"
        var retrofit = Retrofit.Builder().baseUrl(rutaJuegos).addConverterFactory(
            GsonConverterFactory.create()).build()
        var interfaz: JuegoAPIInterface = retrofit.create(JuegoAPIInterface::class.java)
        println("---------------------------------------------------------------------")
        videogameList = getJuegos(interfaz)
        println("Llamada")
        println("---------------------------------------------------------------------")

        recyclerVideogame = root.findViewById(R.id.recyclerVideogames)
        recyclerVideogame.layoutManager = LinearLayoutManager(root.context)
        adapterVideogame = AdapterVideogame(videogameList, function, functionDelete, functionUpdate)
        recyclerVideogame.adapter = adapterVideogame

        createvideogame = root.findViewById(R.id.createvideogame)
        createvideogame.setOnClickListener {
            val sendIntent = Intent(requireContext(), CreateVideogame::class.java)
            responseLauncher.launch(sendIntent)
        }

        filtervideogames = root.findViewById(R.id.filtervideogames)
        filterVideogames()//Método que filtra los videojuegos.
        return root
    }

    // Recoge la url almacenada en memoria.
    private fun leerFicherosAjustes() {
        val rutaAplicacion = requireActivity().applicationContext.filesDir.toString()
        val filename = "Conection.txt"
        val ruta = File(rutaAplicacion)
        val fichero = File(ruta, filename)
        println("--------------------------------------------")
        if (fichero.exists()) {
            val bufferedReader = BufferedReader(FileReader(fichero))
            val stringBuilder = StringBuilder()
            bufferedReader.useLines { lines ->
                lines.forEach {
                    stringBuilder.append("$it\n")
                }
            }
            val fileContents = stringBuilder.toString()
            println(fileContents)
            url = fileContents
        } else {
            url = "http://192.168.1.1:8080"
        }
    }


    // Metodos CRUD
    private fun createJuegos(interfaz: JuegoAPIInterface, juego:Videogame) {
        var id = juego.id
        var title = juego.title
        var developer = juego.developer
        var publisher = juego.publisher
        var europePal = juego.europe
        var japan = juego.japan
        var northAmerica = juego.america
        interfaz.crearJuego(JuegoAPI(id,title,developer,publisher,europePal,japan,northAmerica)).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                println(response.message().toString())
                if (response.code() == 200) {
                    Toast.makeText(context, "Se ha añadido el juego con ID: $id de forma exitosa.",Toast.LENGTH_SHORT).show()
                } else if (response.code() == 409) {
                    Toast.makeText(context, "Este juego con ID: $id ya existe.",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message.toString()}",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getJuegos(interfaz: JuegoAPIInterface):ArrayList<Videogame> {
        var objeto = ArrayList<Videogame>()
        interfaz.obtenerJuegos().enqueue(object : Callback<List<JuegoAPI>> {
            override fun onResponse(call: Call<List<JuegoAPI>>, response: Response<List<JuegoAPI>>) {
                if (response.code() == 200) {
                    println("-----------------------------------------------------------------")
                    println("Obteniendo datos")
                    val cuerpo = response.body()
                    if (cuerpo != null) {
                        for (j in cuerpo) {
                            var id = j.id
                            var title = j.title
                            var developer = j.developer
                            var publisher = j.publisher
                            var europePal = j.europePal
                            var japan = j.japan
                            var northAmerica = j.northAmerica
                            val replace = title?.replace(":", "")
                            var urlImage = url + "api/v1/images/$replace"
                            objeto.add(Videogame(id,title,developer,publisher,europePal,japan,northAmerica,urlImage))
                            println("-----------------------------------------------------------------")
                            println("id: $id")
                            println("title: $title")
                            println("developer: $developer")
                            println("publisher: $publisher")
                            println("europePal: $europePal")
                            println("japan: $japan")
                            println("northAmerica: $northAmerica")
                            println("urlImage: $urlImage")
                            println("-----------------------------------------------------------------")
                            adapterVideogame.videogameUpdate(objeto)
                        }

                    } else {
                        println("-----------------------------------------------------------------")
                        println("No hay datos")
                    }
                } else {
                    println("-----------------------------------------------------------------")
                    println(response.code().toString())
                    Toast.makeText(context, "Error ${response.code()}",Toast.LENGTH_SHORT).show()
                    val cuerpo = response.errorBody()
                    if (cuerpo != null) {
                        println(cuerpo.string())
                    }
                }
                // 400, 300, resto de codigos.
            }
            override fun onFailure(call: Call<List<JuegoAPI>>, t: Throwable) {
                // Gestionar la exepcion
                println("-----------------------------------------------------------------")
                println("Ocurrio un error al contactar el servidor.")
                println(t.message.toString())
                Toast.makeText(context, "Ocurrio un error al contactar el servidor.\n${t.message.toString()}",Toast.LENGTH_SHORT).show()
            }
        })
        return objeto
    }

    private fun putJuegos(interfaz: JuegoAPIInterface, idJuego:Int, juego:Videogame) {
        var id = juego.id
        var title = juego.title
        var developer = juego.developer
        var publisher = juego.publisher
        var europePal = juego.europe
        var japan = juego.japan
        var northAmerica = juego.america
        interfaz.actualizarJuego(idJuego, JuegoAPI(id,title,developer,publisher,europePal,japan,northAmerica)).enqueue(object:
            Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                println(response.message().toString())
                if (response.code() == 200) {
                    Toast.makeText(context, "Se ha añadido el juego con ID: $id de forma exitosa.",Toast.LENGTH_SHORT).show()
                } else if (response.code() == 409) {
                    Toast.makeText(context, "Este juego con ID: $id ya existe.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message.toString()}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteJuegos(interfaz: JuegoAPIInterface, idJuego:Int) {
        interfaz.borrarJuego(idJuego).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                println(response.message().toString())
                if (response.code() == 200) {
                    Toast.makeText(context, "Se ha eliminado el juego con ID: $idJuego de forma exitosa.",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Este juego con ID: $id no existe.",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message.toString()}",Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentVideogames.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentVideogames().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

//---------------------------------Funciones para el AdapterVideogame-------------------------------
//Función que coge los datos del videojuego seleccionado y los muestra en otra actividad.
private val function = object: Capture {
    override fun clickEnElemento(pos: Int) {
        val sendIntent = Intent(requireContext(), InfoVideogame::class.java)
        sendIntent.putExtra("id", videogameList[pos].id)
        sendIntent.putExtra("title", videogameList[pos].title)
        sendIntent.putExtra("developer", videogameList[pos].developer)
        sendIntent.putExtra("publisher", videogameList[pos].publisher)
        sendIntent.putExtra("europe", videogameList[pos].europe)
        sendIntent.putExtra("japan", videogameList[pos].japan)
        sendIntent.putExtra("america", videogameList[pos].america)
        if (videogameList[pos].urlImage.isNullOrBlank()) {
            sendIntent.putExtra("urlImage", "")
        } else {
            sendIntent.putExtra("urlImage", videogameList[pos].urlImage)
        }
        //TODO enviar datos a la actividad que se encarga de visualizar la información del videojuego.
        startActivity(sendIntent)
    }
}

//Función que borra un videojuego gracias que recoge el valor de su posición en la lista.
private val functionDelete = object: DeleteItem {
    override fun deleteItem(pos: Int) {
        var rutaJuegos = url + "gamecube/"
        var retrofit = Retrofit.Builder().baseUrl(rutaJuegos).addConverterFactory(
            GsonConverterFactory.create()).build()
        var interfaz: JuegoAPIInterface = retrofit.create(JuegoAPIInterface::class.java)
        var idJuego = videogameList[pos].id
        //Diálogo para eliminar juego.
        val alert: AlertDialog.Builder = AlertDialog.Builder(context)
        alert.setTitle("Eliminar juego")
        alert.setMessage("Esta accion es irreversible.\n" +
                "¿Esta seguro que desea continuar?");
        alert.setPositiveButton("Ok",
            DialogInterface.OnClickListener { dialog, whichButton ->
                if (idJuego != null) {
                    deleteJuegos(interfaz, idJuego)
                    videogameList.removeAt(idJuego)
                    adapterVideogame.notifyDataSetChanged()
                }
                Toast.makeText(context, "Juego eliminado.", Toast.LENGTH_SHORT).show()
            })
        alert.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, whichButton ->
            })
        alert.show()
    }
}

//Función que coje el Searchview y filtra los videojuegos desde el ArrayList para cambiar el RecyclerView.
private fun filterVideogames() {
    filtervideogames.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            var pokemonFiltered = videogameList.filter { videogame -> videogame.title!!.lowercase().contains(newText.toString() )  }
            var pf = ArrayList(pokemonFiltered)
            adapterVideogame.filterVideogames(pf)

            return false
        }

    })
}

//Función que llama a una actividad y de ésta recibe datos con los que montará e insertará un videojuego en la lista.
private val responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult() ) {
        resultado ->
    if(resultado.resultCode == AppCompatActivity.RESULT_OK) {
        //Recogemos los valores que lleva el Intent.
        val id = resultado.data?.getIntExtra("id", 0)
        val title = resultado.data?.getStringExtra("title")
        val developer = resultado.data?.getStringExtra("developer")
        val publisher  =resultado.data?.getStringExtra("publisher")
        val europe = resultado.data?.getStringExtra("europe")
        val japan = resultado.data?.getStringExtra("japan")
        val america  = resultado.data?.getStringExtra("america")
        if (!title.isNullOrEmpty() || !developer.isNullOrEmpty() || !publisher.isNullOrEmpty()) {

            var rutaJuegos = url + "gamecube/"
            var retrofit = Retrofit.Builder().baseUrl(rutaJuegos).addConverterFactory(
                GsonConverterFactory.create()).build()
            var interfaz: JuegoAPIInterface = retrofit.create(JuegoAPIInterface::class.java)
            var juego = Videogame(id, title, developer, publisher, europe, japan, america)
            createJuegos(interfaz,juego)
            //Añadimos un nuevo registro de Pokemon a la lista de pokemons guardada en el sistema.
            if (id != null) {
                videogameList.add(0,juego)
            }
            //Notificamos al adaptador de los cambios para que recogamos los datos de la nueva lista.
            if (id != null) {
                recyclerVideogame.adapter?.notifyItemInserted(0)
            }
        }

    }
}

//**************************************************************************************************
//Dos funciones que trabajan conjunatamente para actualizar los vidoejuegos.
//**************************************************************************************************

//La primera es la funcion que llama directamente a la activiadad que actualiza el videojuego usando
//un índice, es decir, la posición del videojuego en la lista.

private var index : Int? = null
private val updateLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        resultado ->
    if(resultado.resultCode == AppCompatActivity.RESULT_OK) {
        //Recogemos los valores que lleva el Intent.
        var id = resultado.data?.getIntExtra("id", 0)
        var title = resultado.data?.getStringExtra("title")
        var developer = resultado.data?.getStringExtra("developer")
        var publisher  =resultado.data?.getStringExtra("publisher")
        var europe = resultado.data?.getStringExtra("europe")
        var japan = resultado.data?.getStringExtra("japan")
        var america  = resultado.data?.getStringExtra("america")

        var videogame = videogameList?.get(index!!)
        videogame?.id = id
        videogame?.title = title
        videogame?.developer = developer
        videogame?.publisher = publisher
        videogame?.europe = europe
        videogame?.japan = japan
        videogame?.america = america

        var rutaJuegos = url + "gamecube/"
        var retrofit = Retrofit.Builder().baseUrl(rutaJuegos).addConverterFactory(
            GsonConverterFactory.create()).build()
        var interfaz: JuegoAPIInterface = retrofit.create(JuegoAPIInterface::class.java)
        var juego = Videogame(id, title, developer, publisher, europe, japan, america)
        putJuegos(interfaz,idJuego,juego)
        videogameList.removeAt(index!!)
        videogameList.add(index!!, videogame!!)
        //Notificamos al adaptador de los cambios para que recogamos los datos de la nueva lista.
        recyclerVideogame.adapter?.notifyDataSetChanged()
    }
}

//La segunda función recoge los datos del videojuego seleccionado y su índice y los envía a la
//la actividad designada para la actualización utilizando el método anteriormente mostrado.

private val functionUpdate = object: Update {
    override fun updateItem(pos: Int) {
        index = pos
        val sendIntent = Intent(requireContext(), UpdateVideogame::class.java)
        val selected = videogameList[pos]
        val id = selected.id
        val title = selected.title
        val developer = selected.developer
        val publisher = selected.publisher
        val europe = selected.europe
        val japan = selected.japan
        val america = selected.america
        if (id != null) {
            idJuego = id
        }
        sendIntent.putExtra("id", id)
        sendIntent.putExtra("title", title)
        sendIntent.putExtra("developer", developer)
        sendIntent.putExtra("publisher", publisher)
        sendIntent.putExtra("europe", europe)
        sendIntent.putExtra("japan", japan)
        sendIntent.putExtra("america", america)

        //TODO lanzar la actividad que se encarga de actualizar los datos de un videojuego.
        updateLauncher.launch(sendIntent)
    }
}
//**************************************************************************************************
//------------------------------------Fin para el AdapterVideogame----------------------------------
}