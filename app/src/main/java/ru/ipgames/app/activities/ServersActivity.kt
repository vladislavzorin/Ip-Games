package ru.ipgames.app.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_servers.*
import ru.ipgames.app.R
import ru.ipgames.app.viewModels.ServersViewModel
import ru.ipgames.app.databinding.ActivityServersBinding
import ru.ipgames.app.injection.ServersModelFactory
import ru.ipgames.app.utils.Tools

class ServersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServersBinding
    private lateinit var viewModel: ServersViewModel
    private lateinit var linearLayoutManager:LinearLayoutManager
    private var page:Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initToolbar()
        initComponent()
    }

    fun initBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_servers)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.serversList.layoutManager = linearLayoutManager
        viewModel = ViewModelProviders.of(this, ServersModelFactory(this,getScrollObservable(binding.serversList))).get(ServersViewModel::class.java)
        binding.viewModel=viewModel
    }

    fun initToolbar(){
        setSupportActionBar(maintoolbar)
        supportActionBar!!.title = "Сервера"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        Tools.setSystemBarColor(this)
    }

   fun initComponent(){
      viewModel.isLoading.observe(this, Observer {value ->
         binding.swipeRefreshLayout.isRefreshing = value?:false})
   }

    private fun getScrollObservable(recyclerView: RecyclerView): Observable<Int> {
        return Observable.create { subscriber ->
            val sl = object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val linerLayoutManager = recyclerView.layoutManager!!
                    val count = linerLayoutManager.itemCount
                    val lastVisible = linearLayoutManager.findLastVisibleItemPosition()

                    Log.d("mLog","page = ${page}")
                    if (count < 50) {
                        if (lastVisible >= count - 1) {
                            subscriber.onNext(++page)
                        }
                    } else{
                        if (lastVisible >= count - 1 && count / (50 * page) >= 1) {
                            subscriber.onNext(++page)
                        }
                    }

                }
            }
            recyclerView.addOnScrollListener(sl)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_search -> {
                //val intent = Intent(this, SearchActivity::class.java)
                //startActivity(intent)
                viewModel.onClickFilterButton()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
