package ru.ipgames.app.viewModels

import android.arch.lifecycle.MutableLiveData
import android.support.v7.util.DiffUtil
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.ipgames.app.adapters.ServerAdapter
import ru.ipgames.app.base.BaseViewModel
import ru.ipgames.app.model.Server
import ru.ipgames.app.model.ServerDao
import ru.ipgames.app.network.AppApi
import ru.ipgames.app.utils.ServerDiffUtilCallBackCallback
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ServersViewModel(private val serverDao: ServerDao, private val obs: Observable<Int>): BaseViewModel() {

    @Inject
    lateinit var adapter: ServerAdapter

    @Inject
    lateinit var appApi: AppApi

    val loadingVisibility: MutableLiveData<Boolean> = MutableLiveData()
    var isFirstLoad:Boolean = true
    var isDBObservable:Boolean = false
    var isLoading = MutableLiveData<Boolean>()
    var limit:Int = 50
    private var gameIdFilter:Int = 0
    val mutablePage: MutableLiveData<Int> = MutableLiveData()
    var size = 0
    var isFilterMode:Boolean = false
    private  var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val insertList: ArrayList<Server> = ArrayList()
    private val updateList: ArrayList<Server> = ArrayList()

    fun initFirstSettings(){
        isLoading.value = true
        loadingVisibility.value = true
        mutablePage.value = 1
        size = 0
        isDBObservable = false
        isFirstLoad = true
        loadServers(mutablePage.value!!)
        initScrollSubscriber()
    }

    private fun initScrollSubscriber(){
        compositeDisposable.add(
            obs.observeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .subscribe{newPage->loadServers(newPage)
                                    mutablePage.value = newPage
                }
        )
    }

    fun unSubscribeAll(){
        compositeDisposable.dispose()
        compositeDisposable = CompositeDisposable()
    }

    private fun onRetrievePostListFinish(){
        loadingVisibility.value = false
    }

    fun loadServers(page:Int){

        compositeDisposable.add(
            appApi.getServers(page,limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate {onRetrievePostListFinish()}
                .doOnError{ObservDB()}
                .retryWhen{ob -> ob.take(3).delay(15, TimeUnit.SECONDS)}
                .repeatWhen {ob -> ob.delay(1 , TimeUnit.MINUTES)}
                .subscribe({result -> onResult(result)},{oError()})
        )
    }

    private fun oError(){
        Log.d("mLog","-ERROR-")
    }

    private fun onResult(list:List<Server>)
    {
        var serverList = if(isFilterMode) onFilterList(list) else list

        compositeDisposable.add(
            Observable.fromCallable {serverDao.all}
                .concatMap{ dbPostList ->
                    if(dbPostList.isEmpty()) {
                        serverDao.insertAll(*serverList.toTypedArray())
                        ObservDB()
                    } else {
                        if(isFirstLoad && !isDBObservable) {
                            serverDao.deleteAll()
                            isFirstLoad=false
                            ObservDB()
                        }

                        insertList.clear()
                        updateList.clear()

                        for (i in 0 until serverList.size) {
                            if (serverDao.countID(serverList.toTypedArray()[i].address) == 0) {
                                insertList.add(serverList.toTypedArray()[i])
                            } else {
                                updateList.add(serverList.toTypedArray()[i])
                            }
                        }

                        if (insertList.size > 0) serverDao.insertAll(*insertList.toTypedArray())
                        if (updateList.size > 0) serverDao.updateAll(*updateList.toTypedArray())
                    }
                    Observable.just(null)
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({},{})
        )
    }

    private fun ObservDB(){
        if (!isDBObservable) {
            isDBObservable=true
            val getAll: Disposable = serverDao.all()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ res -> updateRV(res) }, {
                        it.printStackTrace()
                    })

            compositeDisposable.add(getAll)
        }
    }

    private fun updateRV(results:List<Server>)
    {
        if (adapter.itemCount !=0) {
            val diffCallback = ServerDiffUtilCallBackCallback(adapter.getData(), results)
            val diffResult = DiffUtil.calculateDiff(diffCallback, false)
            adapter.updatePostList(results)
            diffResult.dispatchUpdatesTo(adapter)
        } else{
            isLoading.value = false
            adapter.updatePostList(results)
            adapter.notifyDataSetChanged()
        }
    }

    private fun onFilterList(list:List<Server>):List<Server>{
        var serverList = list.filter {server -> server.game_id == gameIdFilter}

        if (size < 25){
            mutablePage.value = mutablePage.value!! + 1
            loadServers(mutablePage.value!!)
            size += serverList.size
        }

        return serverList
    }

    fun setFilterMode(isFilterMode:Boolean, gameIdForFilter:Int){
        this.gameIdFilter = gameIdForFilter
        this.isFilterMode = isFilterMode
    }
}