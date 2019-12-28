package ru.ipgames.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.toolbar.*
import ru.ipgames.app.R
import ru.ipgames.app.databinding.FavoriteServersFragmentBinding
import ru.ipgames.app.viewModels.FavoriteServersFragmentViewModel


class FavoriteServersFragment : Fragment() {

    private lateinit var favoritViewModel: FavoriteServersFragmentViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {return DataBindingUtil.inflate<FavoriteServersFragmentBinding>(inflater,R.layout.favorite_servers_fragment, container, false).root }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        DataBindingUtil.getBinding<FavoriteServersFragmentBinding>(view!!)!!.run {
            favoritViewModel = ViewModelProviders.of(this@FavoriteServersFragment).get(FavoriteServersFragmentViewModel::class.java)
            linearLayoutManager = LinearLayoutManager(
                view!!.context,
                RecyclerView.VERTICAL,
                false
            )

            favoritViewModel.loadingVisibility.observe(activity as AppCompatActivity, Observer{
                    value -> if(value!!) {
                favoriteSwipeRefreshLayout.isRefreshing = value
            }else{
                favoriteSwipeRefreshLayout.isRefreshing = value
                favoriteSwipeRefreshLayout.isEnabled = false
            }
            })

            favoriteServersList.layoutManager = linearLayoutManager
            viewModel = favoritViewModel
        }
        initActionBar()
    }

    private fun initActionBar(){
        (activity as AppCompatActivity).supportActionBar!!.title = "Избранные сервера"

        val layoutParams: AppBarLayout.LayoutParams = (activity as AppCompatActivity)
            .maintoolbar
            .layoutParams as AppBarLayout.LayoutParams

        layoutParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        (activity as AppCompatActivity).maintoolbar.layoutParams = layoutParams
    }

    override fun onDestroyView() {
        super.onDestroyView()
        favoritViewModel.unSubscribeAll()
    }

}