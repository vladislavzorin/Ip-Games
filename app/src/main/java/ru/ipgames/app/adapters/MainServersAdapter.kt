package ru.ipgames.app.adapters

import android.content.Intent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.ipgames.app.R
import ru.ipgames.app.activities.ServerInfoActivity
import ru.ipgames.app.databinding.ItemMainBinding
import ru.ipgames.app.model.Servers

class MainServersAdapter: RecyclerView.Adapter<MainServersAdapter.ViewHolder>() {

    private lateinit var serversList: List<Servers>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainServersAdapter.ViewHolder {
        val binding: ItemMainBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_main, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainServersAdapter.ViewHolder, position: Int) {
        holder.bind(serversList[position])
    }

    override fun getItemCount(): Int {
        return if(::serversList.isInitialized) serversList.size else 0
    }

    fun updatePostList(postList:List<Servers>){
        this.serversList = postList
    }

    fun getData():List<Servers> = serversList

    class ViewHolder(private val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(server: Servers){
            binding.server = server
            binding.RLOfMainitem.setOnClickListener { view->
                val intent = Intent(view.context, ServerInfoActivity::class.java)
                intent.putExtra("server_ip",server.address)
                view.context.startActivity(intent)
            }
        }
    }

}