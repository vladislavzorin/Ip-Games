package ru.ipgames.app.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.ipgames.app.R
import ru.ipgames.app.databinding.ActivityMainBinding
import ru.ipgames.app.fragments.*
import ru.ipgames.app.utils.*
import ru.ipgames.app.viewModels.MainFragmentViewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{


    private lateinit var viewModel: MainFragmentViewModel
    private var errorSnackbar: Snackbar? = null
    private val fm = this.supportFragmentManager

    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFragment()
        initToolbar()
        initComponents()
        initAuth()
    }

    private fun initAuth(){
        mAuth.signInAnonymously()
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.METHOD, "auth")
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)

        UID = "${mAuth.currentUser?.uid}"
    }

    private fun initFragment(){
        fm.beginTransaction()
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
            R.id.nav_home -> {
                fm.beginTransaction()
                    .apply { replace(R.id.fragmentLayout,MainFragment()) }
                    .addToBackStack(null)
                    .commit()
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.nav_servers -> {
                fm.beginTransaction()
                    .apply { replace(R.id.fragmentLayout, ServersListFragment()) }
                    .addToBackStack(null)
                    .commit()
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.nav_search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_add -> {
                fm.beginTransaction()
                    .apply { replace(R.id.fragmentLayout, AddYourServerFragment())}
                    .addToBackStack(null)
                    .commit()
                drawer_layout.closeDrawer(GravityCompat.START)
            }

            R.id.nav_favorite ->{
                fm.beginTransaction()
                    .apply { replace(R.id.fragmentLayout, FavoriteServersFragment())}
                    .addToBackStack(null)
                    .commit()
                drawer_layout.closeDrawer(GravityCompat.START)
            }

            R.id.nav_csgo ->replaceServersListFragmentWithSortByGame(5)
            R.id.nav_cs ->replaceServersListFragmentWithSortByGame(2)
            R.id.nav_tf2 ->replaceServersListFragmentWithSortByGame(11)
            R.id.nav_l4d2 ->replaceServersListFragmentWithSortByGame(10)
            R.id.nav_css ->replaceServersListFragmentWithSortByGame(3)
            R.id.nav_minecraft ->replaceServersListFragmentWithSortByGame(19)
            R.id.nav_gta ->replaceServersListFragmentWithSortByGame(18)
            R.id.nav_ts ->replaceServersListFragmentWithSortByGame(29)
            R.id.nav_arma3 ->replaceServersListFragmentWithSortByGame(28)
            R.id.nav_l4d ->replaceServersListFragmentWithSortByGame(9)
            R.id.nav_gm ->replaceServersListFragmentWithSortByGame(12)
            R.id.nav_mta ->replaceServersListFragmentWithSortByGame(24)
            R.id.nav_bf4 ->replaceServersListFragmentWithSortByGame(23)
            R.id.nav_rust ->replaceServersListFragmentWithSortByGame(14)
            R.id.nav_se ->replaceServersListFragmentWithSortByGame(34)
            R.id.nav_unturned ->replaceServersListFragmentWithSortByGame(31)
            R.id.nav_dod ->replaceServersListFragmentWithSortByGame(6)
            R.id.nav_7dod ->replaceServersListFragmentWithSortByGame(22)
            R.id.nav_kf2 ->replaceServersListFragmentWithSortByGame(32)
            R.id.nav_dayz ->replaceServersListFragmentWithSortByGame(27)
        }
        return true
    }

    private fun initToolbar(){
        setSupportActionBar(maintoolbar)
        Tools.setSystemBarColor(this)
    }

    private fun initComponents(){
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
            R.id.action_goToSite -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL)))
            R.id.action_goToForum -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(FORUM_URL)))
            R.id.action_goToBuy-> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(BUY_URL)))
            R.id.action_goToCheakServer-> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(CHEAK_SERVER_URL)))

            R.id.action_about ->{
                fm.beginTransaction()
                    .apply { replace(R.id.fragmentLayout, AboutFragment()) }
                    .addToBackStack(null)
                    .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun replaceServersListFragmentWithSortByGame(gameId:Int){
        fm.beginTransaction()
            .apply { replace(R.id.fragmentLayout, ServersListFragment(gameId))}
            .addToBackStack(null)
            .commit()
        drawer_layout.closeDrawer(GravityCompat.START)
    }
}
