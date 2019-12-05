package ru.ipgames.app.fragments

import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.hostings_fragment.*
import ru.ipgames.app.R
import ru.ipgames.app.databinding.HostingsFragmentBinding
import ru.ipgames.app.viewModels.HostingsFragmentViewModel

class HostingsFragment : Fragment() {

    companion object {
        fun newInstance() = HostingsFragment()
    }

    private lateinit var fragmentViewModel: HostingsFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<HostingsFragmentBinding>(inflater,R.layout.hostings_fragment, container, false).root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentViewModel = ViewModelProviders.of(this).get(HostingsFragmentViewModel::class.java)

        DataBindingUtil.getBinding<HostingsFragmentBinding>(view!!)!!.run{
            hosting_info_list.layoutManager = LinearLayoutManager(
                view!!.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            viewModel = fragmentViewModel
        }

        initActionBar()
    }

    fun initActionBar(){
        (activity as AppCompatActivity).supportActionBar!!.title = "Хостинги"

        val layoutParams: AppBarLayout.LayoutParams = (activity as AppCompatActivity)
                                                        .maintoolbar
                                                        .layoutParams as AppBarLayout.LayoutParams
        layoutParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        (activity as AppCompatActivity).maintoolbar.layoutParams = layoutParams
    }

}
