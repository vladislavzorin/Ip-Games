package ru.ipgames.app.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ipgames.app.model.InfoAboutPlayer

class OnlinePlayersViewModel: ViewModel(){
    var name = MutableLiveData<String>()
    var score = MutableLiveData<String>()
    var time = MutableLiveData<String>()

    fun bind(player: InfoAboutPlayer)
    {
        name.value = player.name
        score.value = player.score.toString()
        time.value = "Время на сервере: ${player.time}"
    }

}