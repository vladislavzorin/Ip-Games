package ru.ipgames.app.viewModels

import android.arch.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.ipgames.app.adapters.GamesFragmentAdapter
import ru.ipgames.app.base.BaseViewModel
import ru.ipgames.app.model.InfoAboutGame
import ru.ipgames.app.network.AppApi
import javax.inject.Inject

class GamesFragmentViewModel : BaseViewModel() {

    @Inject
    lateinit var appApi: AppApi

    var adapter = GamesFragmentAdapter()

    init{
        loadInfoAboutGames()
    }

    fun loadInfoAboutGames(){
        appApi.getAllGames()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({games -> onResult(games.result)},{it.printStackTrace()})
    }

    fun onResult(list:List<InfoAboutGame>){
        adapter.setGames(list as MutableList<InfoAboutGame>)
        adapter.notifyDataSetChanged()
    }
}
