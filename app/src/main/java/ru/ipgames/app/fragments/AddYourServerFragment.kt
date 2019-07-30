package ru.ipgames.app.fragments

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.toolbar.*
import ru.ipgames.app.R
import ru.ipgames.app.databinding.AddYourServerFragmentBinding
import ru.ipgames.app.viewModels.AddYourServerViewModel

class AddYourServerFragment : Fragment() {
    private lateinit var viewModelFragment: AddYourServerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return DataBindingUtil.inflate<AddYourServerFragmentBinding>(inflater, R.layout.add_your_server_fragment, container, false).root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initBinding()
    }

    fun initBinding(){
        viewModelFragment  = ViewModelProviders.of(this).get(AddYourServerViewModel::class.java)
        DataBindingUtil.getBinding<AddYourServerFragmentBinding>(view!!)!!.run{
            viewModel = viewModelFragment
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
}