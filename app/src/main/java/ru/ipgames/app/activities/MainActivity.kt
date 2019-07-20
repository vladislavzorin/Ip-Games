package ru.ipgames.app.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_post_list.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.ipgames.app.R
import ru.ipgames.app.databinding.ActivityPostListBinding
import ru.ipgames.app.injection.ViewModelFactory
import ru.ipgames.app.utils.Tools
import ru.ipgames.app.viewModels.PostListViewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityPostListBinding
    private lateinit var viewModel: PostListViewModel
    private var errorSnackbar: Snackbar? = null
    private lateinit var linearLayoutManager:LinearLayoutManager
    var page:Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initToolbar()
        initComponents()
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

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_list)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.postList.layoutManager = linearLayoutManager
        binding.hostingList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.gamesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(PostListViewModel::class.java)
        viewModel.errorMessage.observe(this, Observer {
                errorMessage -> if(errorMessage != null) showError(errorMessage) else hideError()
        })
        binding.viewModel = viewModel
    }

    private fun initToolbar(){
        setSupportActionBar(maintoolbar)
        supportActionBar?.title = "Главная"
        Tools.setSystemBarColor(this)
    }

    fun initComponents(){
      /*
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
*/
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

    private fun getScrollObservable(recyclerView: RecyclerView): Observable<Int> {
        return Observable.create { subscriber->
            val sl = object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val linerLayoutManager=recyclerView.layoutManager!!
                    val count = linerLayoutManager.itemCount
                    val lastVisible=linearLayoutManager.findLastVisibleItemPosition()

                    if (lastVisible>=count-1 && count/(50*page)>=1){
                        subscriber.onNext(++page)
                    }
                }
            }
            recyclerView.addOnScrollListener(sl)
        }
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
