package ru.ipgames.app.viewModels

import android.arch.lifecycle.MutableLiveData
import android.support.v7.util.DiffUtil
import android.util.Log
import android.view.View
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.ipgames.app.adapters.PostListAdapter
import ru.ipgames.app.R
import ru.ipgames.app.base.BaseViewModel
import ru.ipgames.app.model.Post
import ru.ipgames.app.model.PostDao
import ru.ipgames.app.network.PostApi
import ru.ipgames.app.utils.ServerDiffUtilCallBackCallback
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ServersViewModel(private val postDao: PostDao, private val obs: Observable<Int>): BaseViewModel() {

    @Inject
    lateinit var adapter: PostListAdapter

    @Inject
    lateinit var postApi: PostApi

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { }
    var isFirstLoad:Boolean = true
    val message:String = "Показать 1234"
    var isDBObservable:Boolean = false
    private lateinit var subscription: Disposable

    var isLoading = MutableLiveData<Boolean>()

    init{
        isLoading.value = true
        loadServers(1)
        initScrollSubscriber()

    }

    fun initScrollSubscriber(){
        obs.observeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .subscribe{res->loadServers(res)}
    }

    fun getMes()=message


    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    private fun onRetrievePostListStart(){
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrievePostListFinish(){
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListSuccess(postList:List<Post>){
        Log.d("mLog"," -ОБНОВЛЕНИЕ-")
       adapter.updatePostList(postList)
    }

    private fun onRetrievePostListError(){
        Log.d("mLog","ERROR")
        errorMessage.value = R.string.post_error
    }

    private fun loadServers(page:Int){

        Log.d("mLog","-loadServers()-")
        subscription = postApi.getPosts(page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { onRetrievePostListFinish()}
                .doOnError{ ObservDB()}
                .retryWhen{ob -> ob.take(3).delay(15, TimeUnit.SECONDS)}
                .repeatWhen {ob -> ob.delay(1 , TimeUnit.MINUTES)}
                .subscribe({result -> onResult(result)
                            },
                        {oError()})
    }

    fun  oError(){
        Log.d("mLog","-ERROR-")
    }

    fun onResult(postList:List<Post>)
    {
        val su = Observable.fromCallable {postDao.all}
                .concatMap{
                    dbPostList ->
                    if(dbPostList.isEmpty()) {
                        Log.d("mLog","isEmpty()")
                        postDao.insertAll(*postList.toTypedArray())
                    } else { if(isFirstLoad && !isDBObservable) {
                        postDao.deleteAll()
                        isFirstLoad=false
                        Log.d("mLog", "-УДАЛЕНИЕ-")

                        ObservDB()
                    }

                        val insertList: ArrayList<Post> = ArrayList<Post>()
                        val updateList: ArrayList<Post> = ArrayList<Post>()
                        Log.d("mLog", "else isEmpty()")
                        for (i in 0..postList.size-1) {
                            if (postDao.countID(postList.toTypedArray().get(i).address) == 0) {
                                insertList.add(postList.toTypedArray().get(i))
                            } else {
                                updateList.add(postList.toTypedArray().get(i))
                            }
                        }

                        if (insertList.size > 0) {
                            postDao.insertAll(*insertList.toTypedArray())
                            Log.d("mLog", "insertList.size=${insertList.size}")
                        }
                        if (updateList.size > 0) {
                            postDao.updateAll(*updateList.toTypedArray())
                            Log.d("mLog", "updateList.size=${updateList.size}")
                        }

                    }
                    Observable.just(postDao.all)
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ Log.d("mLog","-OKDB-")},{ Log.d("mLog","-ERRORDB-")})
    }

    fun ObservDB(){
        //реализовать однократную загрузку
        if (!isDBObservable) {
            isDBObservable=true
            Log.d("mLog", "ObservDB()")
            val getAll: Disposable? = postDao.all()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ res -> updateRV(res) }, {
                        Log.d("mLog", "-getAll ERROR-")
                        it.printStackTrace()
                    })
        }
    }

    fun updateRV(results:List<Post>)
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

    fun onClickFilterButton(){
        var list = adapter.getData().filter {server -> server.game_id == 5}
        adapter.updatePostList(list)
        adapter.notifyDataSetChanged()
    }

}