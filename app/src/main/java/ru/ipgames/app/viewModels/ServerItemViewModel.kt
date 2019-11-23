package ru.ipgames.app.viewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.view.View
import ru.ipgames.app.activities.ServerInfoActivity
import ru.ipgames.app.model.Players
import ru.ipgames.app.model.Server

class ServerItemViewModel: ViewModel() {
    private val postTitle = MutableLiveData<String>()
    private val postBody = MutableLiveData<String>()
    private val nowPlayers = MutableLiveData<Players>()
    private val gameID = MutableLiveData<String>()
    private lateinit var server_ip:String

    fun bind(server: Server){
        postTitle.value = server.name
        postBody.value = server.map.name
        nowPlayers.value = server.players
        gameID.value = server.game_id.toString()
        server_ip=server.address
    }

    fun getPostTitle():MutableLiveData<String>{
        return postTitle
    }

    fun getPostBody():MutableLiveData<String>{
        return postBody
    }

    fun getPlayers():MutableLiveData<Players>{
        return nowPlayers
    }

    fun getServerInfo(view: View){
        val intent = Intent(view.context, ServerInfoActivity::class.java)
        intent.putExtra("server_ip",server_ip)
        view.context.startActivity(intent)
    }

    fun getGameID():MutableLiveData<String>{
        return gameID
    }


}