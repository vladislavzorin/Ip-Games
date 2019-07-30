package ru.ipgames.app.viewModels

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.ipgames.app.adapters.HostingsFragmentAdapter
import ru.ipgames.app.base.BaseViewModel
import ru.ipgames.app.model.InfoAboutHosting
import ru.ipgames.app.network.AppApi
import javax.inject.Inject

class HostingsFragmentViewModel : BaseViewModel() {

    @Inject
    lateinit var appApi: AppApi

    var adapter = HostingsFragmentAdapter()

    init{
        loadHostings()
    }

    fun loadHostings(){
        appApi.getAllHosting()
            .flatMap { listHosting -> Observable.fromIterable(listHosting) }
            .flatMap { hosting -> appApi.getInfoAboutHosting(hosting.hosting_id)}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {info -> onResult(info)},
                {Log.d("mLog","loadHostings() is ERROR")
                it.printStackTrace()})
    }

    fun onResult(info: InfoAboutHosting){
        Log.d("mLog","adapter.addInfoAboutHosting(info)")
        adapter.addInfoAboutHosting(info)
        Log.d("mLog","adapter.notifyDataSetChanged()")
        adapter.notifyDataSetChanged()
    }

}
