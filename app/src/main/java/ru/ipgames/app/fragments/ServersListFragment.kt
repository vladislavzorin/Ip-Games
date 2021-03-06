package ru.ipgames.app.fragments

import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import kotlinx.android.synthetic.main.toolbar.*
import ru.ipgames.app.R
import ru.ipgames.app.databinding.ServersListFragmentBinding
import ru.ipgames.app.injection.ServersModelFactory
import ru.ipgames.app.viewModels.ServersViewModel

class ServersListFragment() : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private var page:Int = 1
    private var gameId:Int = 0
    private var isFilterMode:Boolean = false
    private lateinit var serversViewModel: ServersViewModel

    @SuppressLint("ValidFragment")
    constructor(gameId:Int):this(){
        this.gameId = gameId
        this.isFilterMode = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<ServersListFragmentBinding>(inflater,R.layout.servers_list_fragment, container, false).root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        DataBindingUtil.getBinding<ServersListFragmentBinding>(view!!)!!.run{
            serversViewModel = ViewModelProviders.of(this@ServersListFragment,ServersModelFactory(activity as AppCompatActivity,getScrollObservable(serversList)))
                .get(ServersViewModel::class.java)

            serversViewModel.loadingVisibility.observe(activity as AppCompatActivity, Observer{
                value -> if(value!!) {
                                            swipeRefreshLayout.isRefreshing = value
                                    }else{
                                            swipeRefreshLayout.isRefreshing = value
                                            swipeRefreshLayout.isEnabled = false
                                    }
            })
            linearLayoutManager = LinearLayoutManager(
                view!!.context,
                RecyclerView.VERTICAL,
                false
            )
            serversList.layoutManager = linearLayoutManager
            serversViewModel.mutablePage.observe(this@ServersListFragment,Observer{ page = it?:1 })
            serversViewModel.setFilterMode(isFilterMode,gameId)
            serversViewModel.initFirstSettings()
            viewModel = serversViewModel
        }
        initActionBar()
    }

    private fun initActionBar(){
        (activity as AppCompatActivity).supportActionBar!!.title = "Сервера"

        val layoutParams: AppBarLayout.LayoutParams = (activity as AppCompatActivity)
            .maintoolbar
            .layoutParams as AppBarLayout.LayoutParams

        layoutParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        (activity as AppCompatActivity).maintoolbar.layoutParams = layoutParams
    }

    private fun getScrollObservable(recyclerView: RecyclerView): Observable<Int> {
        return Observable.create { subscriber ->
            val sl = object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val linerLayoutManager = recyclerView.layoutManager!!
                    val count = linerLayoutManager.itemCount
                    val lastVisible = linearLayoutManager.findLastVisibleItemPosition()

                    if (count < 50 * page) {
                        if (lastVisible >= count - 1) {
                            subscriber.onNext(++page)
                        }
                    } else {
                           if (lastVisible >= count - 1 && count / (50 * page) >= 1) {
                               subscriber.onNext(++page)
                            }
                    }
                }
            }
            recyclerView.addOnScrollListener(sl)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        serversViewModel.unSubscribeAll()
    }
}
