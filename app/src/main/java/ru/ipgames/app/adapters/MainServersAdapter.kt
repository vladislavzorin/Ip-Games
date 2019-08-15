package ru.ipgames.app.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.ipgames.app.R
import ru.ipgames.app.databinding.ItemMainBinding
import ru.ipgames.app.model.Servers
import ru.ipgames.app.viewModels.ServersItemViewModel

class MainServersAdapter: RecyclerView.Adapter<MainServersAdapter.ViewHolder>() {

    private lateinit var postList: List<Servers>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainServersAdapter.ViewHolder {
        val binding: ItemMainBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_main, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainServersAdapter.ViewHolder, position: Int) {
        holder.bind(postList[position])
    }

    override fun getItemCount(): Int {
        return if(::postList.isInitialized) postList.size else 0
    }

    fun updatePostList(postList:List<Servers>){
        this.postList = postList
    }

    fun getData():List<Servers> = postList

    class ViewHolder(private val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root){
        private val viewModel = ServersItemViewModel()

        fun bind(post: Servers){
            viewModel.bind(post)
            binding.viewModel = viewModel
        }
    }

}