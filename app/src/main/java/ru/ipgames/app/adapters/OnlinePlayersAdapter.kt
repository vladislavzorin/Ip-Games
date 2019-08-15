package ru.ipgames.app.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.ipgames.app.R
import ru.ipgames.app.databinding.ItemOnlinePlayersBinding
import ru.ipgames.app.viewModels.OnlinePlayersViewModel
import ru.ipgames.app.model.InfoAboutPlayer

class OnlinePlayersAdapter: RecyclerView.Adapter<OnlinePlayersAdapter.ViewHolder>() {

    private lateinit var playersList: List<InfoAboutPlayer>

    fun setPlayersList(playersList: List<InfoAboutPlayer>){
        this.playersList = playersList
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(playersList[position])
    }

    override fun getItemCount(): Int {
        return if(::playersList.isInitialized) playersList.size else 0
    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val binding: ItemOnlinePlayersBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_online_players, parent, false)
        return ViewHolder(binding)
    }


    class ViewHolder(private val binding: ItemOnlinePlayersBinding): RecyclerView.ViewHolder(binding.root){
        private val viewModel = OnlinePlayersViewModel()

        fun bind(infoAboutPlayer: InfoAboutPlayer){
            viewModel.bind(infoAboutPlayer)
            binding.viewModel = viewModel
        }
    }
}





