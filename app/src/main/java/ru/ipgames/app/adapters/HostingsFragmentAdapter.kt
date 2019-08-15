package ru.ipgames.app.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.ipgames.app.R
import ru.ipgames.app.databinding.ItemFragmentHostingsBinding
import ru.ipgames.app.model.InfoAboutHosting
import ru.ipgames.app.viewModels.HostingsFragmentAdapterViewModel

class HostingsFragmentAdapter: RecyclerView.Adapter<HostingsFragmentAdapter.ViewHolder>() {
    var list:ArrayList<InfoAboutHosting> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
       val binding:ItemFragmentHostingsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_fragment_hostings, parent, false)
        return HostingsFragmentAdapter.ViewHolder(binding)
    }

    override fun getItemCount(): Int = if(list.size>0) list.size else 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position],position)
    }

    fun addInfoAboutHosting(infoAboutHosting: InfoAboutHosting){
        list.add(infoAboutHosting)
    }

    class ViewHolder(private var binding:ItemFragmentHostingsBinding): RecyclerView.ViewHolder(binding.root){
        private var viewModel = HostingsFragmentAdapterViewModel()

        fun bind(info:InfoAboutHosting,position: Int){
            viewModel.bind(info,position)
            binding.viewModel = viewModel
        }
    }
}

