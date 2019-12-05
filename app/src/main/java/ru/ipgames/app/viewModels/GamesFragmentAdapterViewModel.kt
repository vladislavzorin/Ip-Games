package ru.ipgames.app.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.view.View
import ru.ipgames.app.model.InfoAboutGame

class GamesFragmentAdapterViewModel: ViewModel() {
    var players = MutableLiveData<String>()
    var position = MutableLiveData<Int>()
    var servers = MutableLiveData<String>()
    var name = MutableLiveData<String>()
    var id = MutableLiveData<String>()
    var isIconVisibility = MutableLiveData<Int>()

    fun bind(game: InfoAboutGame,itemPosition:Int){
        players.value = game.players.toString()
        position.value = itemPosition
        servers.value = game.servers.toString()
        name.value = if(game.name.compareTo("All")==0) "Всего серверов в мониторинге" else game.name
        id.value = game.id.toString()
        isIconVisibility.value = if(game.id == 1) View.GONE  else View.VISIBLE
    }
}