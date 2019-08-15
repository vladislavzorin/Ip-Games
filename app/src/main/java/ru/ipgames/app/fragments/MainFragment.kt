package ru.ipgames.app.fragments

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.toolbar.*
import ru.ipgames.app.R
import ru.ipgames.app.databinding.MainFragmentBinding
import ru.ipgames.app.injection.ViewModelFactory
import ru.ipgames.app.viewModels.MainFragmentViewModel

class MainFragment : Fragment() {

    private lateinit var model: MainFragmentViewModel

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return DataBindingUtil.inflate<MainFragmentBinding>(inflater,R.layout.main_fragment, container, false).root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initBinding()
    }

    fun initBinding(){
        Log.d("mLog","initBinding()")
        model  = ViewModelProviders.of(this, ViewModelFactory(activity as AppCompatActivity)).get(MainFragmentViewModel::class.java)
        model.activityFragment = activity as FragmentActivity

        DataBindingUtil.getBinding<MainFragmentBinding>(view!!)!!.run{
            postList.layoutManager = LinearLayoutManager(view!!.context, LinearLayoutManager.VERTICAL, false)
            hostingList.layoutManager = LinearLayoutManager(view!!.context, LinearLayoutManager.VERTICAL, false)
            gamesList.layoutManager = LinearLayoutManager(view!!.context, LinearLayoutManager.VERTICAL, false)
            model.loadServers2(1)
            viewModel = model
        }
        initActionBar()
    }

    fun initActionBar(){
        (activity as AppCompatActivity).supportActionBar!!.title = "Главная"

        var layoutParams: AppBarLayout.LayoutParams = (activity as AppCompatActivity)
                                                        .maintoolbar
                                                        .layoutParams as AppBarLayout.LayoutParams
        layoutParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        (activity as AppCompatActivity).maintoolbar.layoutParams = layoutParams
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ServersViewModel","onDestroyView()")
        DataBindingUtil.getBinding<MainFragmentBinding>(view!!)!!.run{
            model.unSubscribeAll()
            viewModel = model
        }
    }


}
