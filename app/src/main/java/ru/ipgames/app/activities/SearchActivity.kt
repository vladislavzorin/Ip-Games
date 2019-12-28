package ru.ipgames.app.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.ipgames.app.App
import ru.ipgames.app.R
import ru.ipgames.app.network.AppApi
import ru.ipgames.app.utils.Tools
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {
    @Inject
    lateinit var appApi: AppApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        App.component().inject(this)

        initToolBar()
        initComponent()
        adView.loadAd(App.getAdRequest())
    }

    private fun initToolBar(){
        setSupportActionBar(maintoolbar)
        supportActionBar!!.title = null
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    private fun initComponent(){
        et_search.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                searchAction()
                return@setOnEditorActionListener false
            }
            return@setOnEditorActionListener true
        }

        fab.setOnClickListener {searchAction()}
    }

    fun searchAction() {
        progress_bar.visibility = View.VISIBLE
        fab.alpha = 0f

        appApi.getInfoAboutServer(et_search.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({result ->
                    if (result.result.status.toInt()!=0 && result.result.server_id !=0){
                        val intent = Intent(this, ServerInfoActivity::class.java)
                        intent.putExtra("server_ip",et_search.text.toString())
                        startActivity(intent)
                        fab.alpha = 1f
                        progress_bar.visibility = View.GONE
                    }else{
                        fab.alpha = 1f
                        progress_bar.visibility = View.GONE
                        Toast.makeText(this,"IP сервера введено не корректно",Toast.LENGTH_LONG).show()
                    }

                },{
                    fab.alpha = 1f
                    Toast.makeText(this,"IP сервера введено не корректно",Toast.LENGTH_LONG).show()
                })

    }

    fun hideKeyboard(){
        val view = this.currentFocus
        if (view != null) {
            val imm:InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
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
