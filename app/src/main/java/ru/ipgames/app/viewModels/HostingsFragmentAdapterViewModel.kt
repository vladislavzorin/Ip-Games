package ru.ipgames.app.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ipgames.app.model.InfoAboutHosting

class HostingsFragmentAdapterViewModel:ViewModel() {
    var players = MutableLiveData<String>()
    var position = MutableLiveData<Int>()
    var servers = MutableLiveData<String>()
    var name = MutableLiveData<String>()
    var site = MutableLiveData<String>()

    fun bind(info:InfoAboutHosting,numberOfPosition:Int){
        players.value = info.result.players.toString()
        position.value = numberOfPosition
        servers.value= info.result.servers.toString()
        name.value= info.result.name
        site.value= info.result.site
    }

}