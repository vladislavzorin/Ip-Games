package ru.ipgames.app.adapters

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.ipgames.app.R
import ru.ipgames.app.databinding.ItemFragmentGamesBinding
import ru.ipgames.app.model.InfoAboutGame
import ru.ipgames.app.viewModels.GamesFragmentAdapterViewModel

class GamesFragmentAdapter: RecyclerView.Adapter<GamesFragmentAdapter.ViewHolder>() {
    private lateinit var gamesList:MutableList<InfoAboutGame>

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val binding:ItemFragmentGamesBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_fragment_games, parent, false)
        return GamesFragmentAdapter.ViewHolder(binding)
    }

    override fun getItemCount(): Int = if(::gamesList.isInitialized) gamesList.size else 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(gamesList[position],position)
    }

    fun setGames(list:MutableList<InfoAboutGame>){
        this.gamesList = list
    }

    class ViewHolder(private var binding:ItemFragmentGamesBinding): RecyclerView.ViewHolder(binding.root){
        private var viewModel = GamesFragmentAdapterViewModel()

        fun bind(game:InfoAboutGame,position: Int){
            viewModel.bind(game = game,itemPosition = position)
            binding.viewModel = viewModel
        }
    }

}