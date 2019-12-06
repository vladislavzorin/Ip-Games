package ru.ipgames.app.fragments

import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.main_fragment.*
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

    private fun initBinding(){
        model  = ViewModelProviders.of(this, ViewModelFactory(activity as AppCompatActivity)).get(MainFragmentViewModel::class.java)
        model.activityFragment = activity as FragmentActivity

        DataBindingUtil.getBinding<MainFragmentBinding>(view!!)!!.run{
            postList.layoutManager = LinearLayoutManager(
                view!!.context,
                RecyclerView.VERTICAL,
                false
            )
            hostingList.layoutManager = LinearLayoutManager(
                view!!.context,
                RecyclerView.VERTICAL,
                false
            )
            gamesList.layoutManager = LinearLayoutManager(
                view!!.context,
                RecyclerView.VERTICAL,
                false
            )

            model.loadServers2(1)
            viewModel = model
        }
        initActionBar()
    }

    private fun initActionBar(){
        (activity as AppCompatActivity).supportActionBar!!.title = "Главная"

        val layoutParams: AppBarLayout.LayoutParams = (activity as AppCompatActivity)
                                                        .maintoolbar
                                                        .layoutParams as AppBarLayout.LayoutParams
        layoutParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        (activity as AppCompatActivity).maintoolbar.layoutParams = layoutParams
    }


    override fun onDestroyView() {
        super.onDestroyView()
        model.unSubscribeAll()
    }


}
