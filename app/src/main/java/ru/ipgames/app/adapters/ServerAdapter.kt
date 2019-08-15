package ru.ipgames.app.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.ipgames.app.R
import ru.ipgames.app.viewModels.PostViewModel
import ru.ipgames.app.databinding.ItemPostBinding
import ru.ipgames.app.model.Server

class ServerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var serverList: List<Server>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding: ItemPostBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_post, parent, false)
            ViewHolder(binding)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            LoadingHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) holder.bind(serverList[position])
    }

    override fun getItemCount(): Int {
        return if(::serverList.isInitialized) serverList.size else 0
    }

    fun updatePostList(serverList:List<Server>){
        this.serverList = serverList
    }

    override fun getItemViewType(position: Int): Int {
        return if(::serverList.isInitialized) {
                    if(serverList.size-1 <= position) {1} else {0}
                } else{1}
    }

    fun getData():List<Server> = serverList

    class ViewHolder(private val binding: ItemPostBinding):RecyclerView.ViewHolder(binding.root){
        private val viewModel = PostViewModel()

        fun bind(server:Server){
            viewModel.bind(server)
            binding.viewModel = viewModel
        }
    }

    class LoadingHolder(view: View): RecyclerView.ViewHolder(view)

}