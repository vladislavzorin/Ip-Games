package ru.ipgames.app.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import io.reactivex.Observable
import kotlinx.android.synthetic.main.toolbar.*
import ru.ipgames.app.R
import ru.ipgames.app.viewModels.PostListViewModel
import ru.ipgames.app.databinding.ActivityPostListBinding
import ru.ipgames.app.injection.ViewModelFactory
import ru.ipgames.app.utils.Tools

class PostListActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPostListBinding
    private lateinit var viewModel: PostListViewModel
    private var errorSnackbar: Snackbar? = null
    private lateinit var linearLayoutManager:LinearLayoutManager
    var page:Int = 1

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        initBinding()
        initToolbar()
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
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