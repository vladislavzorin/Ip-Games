package ru.ipgames.app.viewModels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.ipgames.app.adapters.FavoriteServerAdapter
import ru.ipgames.app.base.BaseViewModel
import ru.ipgames.app.model.FavoriteServersDao
import ru.ipgames.app.model.Server
import ru.ipgames.app.network.AppApi
import ru.ipgames.app.utils.KEY
import ru.ipgames.app.utils.ServerDiffUtilCallBackCallback
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FavoriteServersFragmentViewModel: BaseViewModel() {

    @Inject
    lateinit var db: FavoriteServersDao

    @Inject
    lateinit var appApi: AppApi

    var isBlockEmptyVisible:MutableLiveData<Int> = MutableLiveData()
    var isRecyclerViewVisible:MutableLiveData<Int> = MutableLiveData()

    var adapter: FavoriteServerAdapter = FavoriteServerAdapter()

    val loadingVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private  var compositeDisposable: CompositeDisposable = CompositeDisposable()

    init{
        loadingVisibility.value = true

        isBlockEmptyVisible.value = View.GONE
        isRecyclerViewVisible.value = View.VISIBLE

        getAllFavoriteServers()
    }

    private fun getAllFavoriteServers(){
        compositeDisposable.add(
            Observable.fromCallable{db.all}
                .concatMap {list ->appApi.getFavoriteServers(key = KEY,servers = list) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate {onRetrieveFinish()}
                .retryWhen{ob -> ob.take(3).delay(15, TimeUnit.SECONDS)}
                .repeatWhen {ob -> ob.delay(1 , TimeUnit.MINUTES)}
                .subscribe({list -> onResult(list)},{it.printStackTrace()})
        )
    }

    private fun onRetrieveFinish(){
        loadingVisibility.value = false
    }

    private fun onResult(list:List<Server>){
        if (list.isEmpty()){
            isBlockEmptyVisible.value = View.VISIBLE
            isRecyclerViewVisible.value = View.GONE
        } else {
            if (adapter.itemCount !=0) {
                val diffCallback = ServerDiffUtilCallBackCallback(adapter.getData(), list)
                val diffResult = DiffUtil.calculateDiff(diffCallback, false)
                adapter.updatePostList(list)
                diffResult.dispatchUpdatesTo(adapter)
            } else{
                adapter.updatePostList(list)
                adapter.notifyDataSetChanged()
            }
        }

    }

    fun unSubscribeAll(){
        compositeDisposable.dispose()
        compositeDisposable = CompositeDisposable()
    }

}