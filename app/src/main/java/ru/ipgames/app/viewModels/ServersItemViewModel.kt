package ru.ipgames.app.viewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.view.View
import ru.ipgames.app.activities.ServerInfoActivity
import ru.ipgames.app.model.Players
import ru.ipgames.app.model.Servers


class ServersItemViewModel: ViewModel() {
    private val postTitle = MutableLiveData<String>()
    private val postBody = MutableLiveData<String>()
    private val gameID = MutableLiveData<String>()
    private val nowPlayers = MutableLiveData<Players>()
    private lateinit var server_ip:String

    fun bind(post: Servers){
        postTitle.value = post.name
        postBody.value = post.map.name
        nowPlayers.value = post.players
        gameID.value = post.game_id.toString()
        server_ip=post.address
    }

    fun getPostTitle(): MutableLiveData<String> {
        return postTitle
    }

    fun getPostBody(): MutableLiveData<String> {
        return postBody
    }

    fun getPlayers(): MutableLiveData<Players> {
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