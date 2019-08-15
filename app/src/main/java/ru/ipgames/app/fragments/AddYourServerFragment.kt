package ru.ipgames.app.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.add_your_server_fragment.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.ipgames.app.R
import ru.ipgames.app.databinding.AddYourServerFragmentBinding
import ru.ipgames.app.utils.ViewAnimation
import ru.ipgames.app.viewModels.AddYourServerViewModel



class AddYourServerFragment : Fragment() {
    private lateinit var viewModelFragment: AddYourServerViewModel

    var viewList:ArrayList<View> = ArrayList()
    var titleList:ArrayList<View> = ArrayList()

    var success_step = 0
    var current_step = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return DataBindingUtil.inflate<AddYourServerFragmentBinding>(inflater, R.layout.add_your_server_fragment, container, false).root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initLists()
        initBinding()
    }

    fun initLists(){
        viewList.add(list)
        viewList.add(ip)
        viewList.add(confirm)

        titleList.add(choose_game)
        titleList.add(edit_ip)
        titleList.add(add_server)
    }

    fun initBinding(){
        viewModelFragment  = ViewModelProviders.of(this).get(AddYourServerViewModel::class.java)
        DataBindingUtil.getBinding<AddYourServerFragmentBinding>(view!!)!!.run{
            initComponent(viewModelFragment)
            viewModel = viewModelFragment
        }
        initActionBar()
    }

    fun initActionBar(){
        (activity as AppCompatActivity).supportActionBar!!.title = "Добавить свой сервер"

        var layoutParams: AppBarLayout.LayoutParams = (activity as AppCompatActivity)
                                                        .maintoolbar
                                                        .layoutParams as AppBarLayout.LayoutParams
        layoutParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        (activity as AppCompatActivity).maintoolbar.layoutParams = layoutParams
    }


    fun collapseAndContinue(number:Int){
        ViewAnimation.collapse(this.viewList[number])
        var nextNumber = number + 1
        current_step = nextNumber
        success_step = if (success_step > nextNumber) nextNumber else success_step
        ViewAnimation.expand(viewList[nextNumber])
    }

    fun initComponent(viewModel: AddYourServerViewModel){
        viewModel.numberOfClick.observe(this, Observer{number ->
            collapseAndContinue(number?:0)
        })

        viewModel.idOfTitleClick.observe(this, Observer{id ->
            when(id){
                R.id.choose_game->{
                    if (success_step >= 0 && current_step != 0) {
                        current_step = 0
                        collapseAll()
                        ViewAnimation.expand(viewList[0])
                    }
                }

                R.id.edit_ip ->{
                    if (success_step >= 1 && current_step != 1) {
                        current_step = 1
                        collapseAll()
                        ViewAnimation.expand(viewList[0])
                    }
                }

                R.id.add_server->{

                }
            }
        })
    }

    fun collapseAll(){
        for (v in viewList) {
            ViewAnimation.collapse(v)
        }
    }


}