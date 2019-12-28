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
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.games_fragment.*
import ru.ipgames.app.R
import ru.ipgames.app.databinding.GamesFragmentBinding
import kotlinx.android.synthetic.main.toolbar.*
import ru.ipgames.app.viewModels.GamesFragmentViewModel

class GamesFragment : Fragment() {

    companion object {
        fun newInstance() = GamesFragment()
    }

    private lateinit var fragmentViewModel: GamesFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return DataBindingUtil.inflate<GamesFragmentBinding>(inflater,R.layout.games_fragment, container, false).root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentViewModel = ViewModelProviders.of(this).get(GamesFragmentViewModel::class.java)
        DataBindingUtil.getBinding<GamesFragmentBinding>(view!!)!!.run{
            games_info_list.layoutManager = LinearLayoutManager(
                view!!.context,
                RecyclerView.VERTICAL,
                false
            )
            viewModel = fragmentViewModel
        }
        initActionBar()
    }

    private fun initActionBar(){
        (activity as AppCompatActivity).supportActionBar!!.title = "Игры"

        val layoutParams: AppBarLayout.LayoutParams = (activity as AppCompatActivity)
                                                        .maintoolbar
                                                        .layoutParams as AppBarLayout.LayoutParams
        layoutParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        (activity as AppCompatActivity).maintoolbar.layoutParams = layoutParams
    }

}
