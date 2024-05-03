package com.example.cliente

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.cliente.fragments.FragmentMap
import com.example.cliente.fragments.FragmentVersion
import com.example.cliente.fragments.FragmentVideogames
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class principal : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar : MaterialToolbar
    private lateinit var navigationview : NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        //"Material Toolbar" personalizada.
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        prepareBottomNavigetionView()
        prepareNavigetionDrawer()
        prepareNavigetionView()
    }

//---------------------------------Preparar Bottom Navigation View----------------------------------
//TODO Código que prepara el Bottomview

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        // By using switch we can easily get the
        // selected fragment by using there id
        lateinit var selectedFragment: Fragment
        when (item.itemId) {
            R.id.videogames -> {
                selectedFragment = FragmentVideogames()
            }
            R.id.version -> {
                selectedFragment = FragmentVersion()
            }
            R.id.map -> {
                selectedFragment = FragmentMap()
            }
        }
        // It will help to replace the
        // one fragment to other.
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
        true
    }

    private fun prepareBottomNavigetionView() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(navListener)

        // as soon as the application opens the first fragment should
        // be shown to the user in this case it is algorithm fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, FragmentVideogames() ).commit()
    }
//------------------------------------Fin Bottom Navigation View------------------------------------

//------------------------------------Preparar Navigation Drawer------------------------------------
//TODO Código que prepara el Drawer

    private fun prepareNavigetionDrawer() {
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById<DrawerLayout>(R.id.my_drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

        // override the onOptionsItemSelected()
        // function to implement
        // the item click listener callback
        // to open and close the navigation
        // drawer when the icon is clicked
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
                true
            } else super.onOptionsItemSelected(item)
        }

//Función que permite a las opciones del "NavigationDrawe" hacer cosas.
    private fun prepareNavigetionView() {

        //TODO Intent ver la clase que muestra ajustes del servidor.
        val ajustes = Intent(applicationContext, Settings::class.java)

        //TODO Intent ver la clase que muestra ajustes de la cuenta
        val cuenta = Intent(applicationContext, Account::class.java)

        navigationview = findViewById<NavigationView>(R.id.navigationview)
        navigationview.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.nav_account -> {
                    startActivity(cuenta)//TODO Cuenta
                    true
                }
                R.id.nav_settings -> {
                    startActivity(ajustes)//TODO Ajustes
                    true
                }
                R.id.nav_logout -> {
                    finish()
                    true
                }
                else -> false
            }
        }
    }
//---------------------------------------Fin Navigation Drawer--------------------------------------
}

/*
private fun handlerND() = object : NavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            if(item.getItemId() == R.id.nav_logout) {
                finish()
            }
            return true
        }
    }
 */