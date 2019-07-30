package ru.ipgames.app.activities

import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.ipgames.app.R
import ru.ipgames.app.databinding.ActivityMainBinding
import ru.ipgames.app.fragments.GamesFragment
import ru.ipgames.app.fragments.HostingsFragment
import ru.ipgames.app.fragments.MainFragment
import ru.ipgames.app.utils.Tools
import ru.ipgames.app.viewModels.MainFragmentViewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainFragmentViewModel
    private var errorSnackbar: Snackbar? = null
    private lateinit var linearLayoutManager:LinearLayoutManager
    var page:Int = 1
    val fm = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFragment()
        initToolbar()
        initComponents()
    }

    fun initFragment(){

        val transaction = fm.beginTransaction()
            .apply { replace(R.id.fragmentLayout, MainFragment()) }
            .commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_camera -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_gallery -> {
                val intent = Intent(this, ServersActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_slideshow -> {
                fm.beginTransaction()
                    .apply { replace(R.id.fragmentLayout, HostingsFragment()) }
                    .commit()
            }
            R.id.nav_manage -> {
                fm.beginTransaction()
                    .apply { replace(R.id.fragmentLayout, GamesFragment()) }
                    .commit()
            }

        }

       // drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initToolbar(){
        setSupportActionBar(maintoolbar)
        Tools.setSystemBarColor(this)
    }

    fun initComponents(){
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, maintoolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun showError(@StringRes errorMessage:Int){
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, viewModel.errorClickListener)
        errorSnackbar?.show()
    }

    private fun hideError(){
        errorSnackbar?.dismiss()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



}
