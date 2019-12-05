package ru.ipgames.app.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.ipgames.app.adapters.OnlinePlayersAdapter
import ru.ipgames.app.base.BaseViewModel
import ru.ipgames.app.model.InfoAboutPlayer
import ru.ipgames.app.model.ResultInfo
import ru.ipgames.app.model.Stats
import ru.ipgames.app.network.AppApi
import javax.inject.Inject

class ServerInfoViewModel: BaseViewModel() {
    @Inject
    lateinit var appApi: AppApi

    @Inject
    lateinit var adapter: OnlinePlayersAdapter

    @SuppressLint("StaticFieldLeak")
    lateinit var context:Context

    var serverIP:String=""
    var name = MutableLiveData<String>()
    var mapName = MutableLiveData<String>()
    var gameid = MutableLiveData<String>()
    var gameName = MutableLiveData<String>()
    var status = MutableLiveData<String>()
    var nowMax = MutableLiveData<String>()
    var location = MutableLiveData<String>()
    var ping = MutableLiveData<String>()
    var urlMapImg = MutableLiveData<String>()
    var Datastats = MutableLiveData<List<Stats>>()
    var sizeRecyclerView= MutableLiveData<Int>()

    var isShowPlayers = MutableLiveData<Int>()
    var isShowRefresh= MutableLiveData<Boolean>()

    var setOnClickBtnCopy = View.OnClickListener {  copyIp() }

    fun loagInfoAboutServer(ip:String){
        Log.d("mLog","load -> $ip")
        isShowRefresh.value = true
        serverIP=ip
        appApi.getInfoAboutServer(ip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({result -> onResult(result)
                            isShowRefresh.value = false},
                        {oError()
                        it.printStackTrace()})

        appApi.getInfoAboutOnlinePlayersInServer(ip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ value -> onResultOnlinePlayers(value.result)
                            sizeRecyclerView.value = value.result.size
                            },
                        { Log.d("mLog","-ERROR getInfoAboutOnlinePlayersInServer-")
                            isShowPlayers.value = View.GONE
                        })

        appApi.getServerStats(ip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({value->onResultStats(value)},
                        { })
    }

    fun  oError(){
        Log.d("mLog","-ERROR-")
    }

    fun onResult(Info: ResultInfo){
        name.value=Info.result.name
        mapName.value=Info.result.map.name
        gameid.value =  Info.result.game.id.toString()
        gameName.value = Info.result.game.name
        status.value = if(Info.result.status.toInt() == 1) "Работает" else "Недоступен"
        nowMax.value = "${Info.result.players.now}/${Info.result.players.max}"
        location.value = Info.result.country.name
        ping.value = Info.result.ping.toString()+"мс"
        urlMapImg.value = Info.result.map.img
    }

    fun onResultOnlinePlayers(players:List<InfoAboutPlayer>){
        adapter.setPlayersList(players)
        isShowPlayers.value = if (players.isNotEmpty()) View.VISIBLE else View.GONE
    }

    fun onResultStats(stats:List<Stats>){
        Log.d("mLog","size=${stats.size}")
        Datastats.value = stats
    }

    fun copyIp(){
        var clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        var clipData = ClipData.newPlainText("clipboard", serverIP)
        clipboard.primaryClip = clipData
        Toast.makeText(context, "IP сервера скопировано", Toast.LENGTH_SHORT).show()
    }
}