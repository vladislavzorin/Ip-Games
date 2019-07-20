package ru.ipgames.app.viewModels

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.support.v7.util.DiffUtil
import android.util.Log
import android.view.View
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.ipgames.app.activities.ServersActivity
import ru.ipgames.app.adapters.HostingAdapter
import ru.ipgames.app.adapters.MainServersAdapter
import ru.ipgames.app.R
import ru.ipgames.app.base.BaseViewModel
import ru.ipgames.app.model.InfoAboutGame
import ru.ipgames.app.model.ListHostings
import ru.ipgames.app.model.Servers
import ru.ipgames.app.model.ServersDao
import ru.ipgames.app.network.PostApi
import ru.ipgames.app.utils.MainServersDiffUtilCallBackCallback
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PostListViewModel(private val postDao: ServersDao): BaseViewModel(){
    @Inject
    lateinit var postApi: PostApi

    @Inject
    lateinit var postListAdapter: MainServersAdapter

    val hostAdapter:HostingAdapter = HostingAdapter()
    val gameAdapter:HostingAdapter = HostingAdapter()

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage:MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { }
    var isFirstLoad:Boolean = true
    var isDBObservable:Boolean = false

    val loadingGameCard = MutableLiveData<Int>()
    val progressGameCard = MutableLiveData<Int>()
    val loadingHostingCard = MutableLiveData<Int>()
    val progressHostingCard = MutableLiveData<Int>()
    val loadingMainCard = MutableLiveData<Int>()
    val progressMainCard = MutableLiveData<Int>()

    private lateinit var subscription: Disposable

    init{
        loadListGames()
        loadListHosting()
        loadServers(1)
    }

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

    private fun onRetrievePostListSuccess(postList:List<Servers>){
        Log.d("mLog"," -ОБНОВЛЕНИЕ-")
        postListAdapter.updatePostList(postList)
    }

    private fun onRetrievePostListError(){
        Log.d("mLog","ERROR")
        errorMessage.value = R.string.post_error
    }

    private fun loadServers(page:Int){
        Log.d("mLog","-loadServers()-")
        subscription = postApi.getServersMain(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate{onRetrievePostListFinish()}
                .doOnError  {ObservDB()}
                .retryWhen  {ob -> ob.take(3).delay(15,TimeUnit.SECONDS)}
                .repeatWhen {ob -> ob.delay(1 , TimeUnit.MINUTES)}
                .subscribe({    result -> onResult(result)
                                loadingMainCard.value = View.VISIBLE
                                progressMainCard.value = View.GONE
                            },
                            {
                                oError()
                            })
    }



    fun  oError(){
        Log.d("mLog","-ERROR-")
    }

    fun onResult(postList:List<Servers>)
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

                            val insertList: ArrayList<Servers> = ArrayList<Servers>()
                            val updateList: ArrayList<Servers> = ArrayList<Servers>()
                            Log.d("mLog", "else isEmpty()")

                            for (i in 0..9) {
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({Log.d("mLog","-OKDB-")},{Log.d("mLog","-ERRORDB-")})
    }

    fun ObservDB(){
        if (!isDBObservable) {
            isDBObservable=true
            Log.d("mLog", "ObservDB()")
            val getAll: Disposable? = postDao.all()
                    .take(10)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ res -> updateRV(res) }, {
                        Log.d("mLog", "-getAll ERROR-")
                        it.printStackTrace()
                    })
        }
    }

    fun updateRV(results:List<Servers>)
    {
        if (postListAdapter.getItemCount()!=0) {
            val diffCallback = MainServersDiffUtilCallBackCallback(postListAdapter.getData(), results)
            val diffResult = DiffUtil.calculateDiff(diffCallback, false)
            postListAdapter.updatePostList(results)
            diffResult.dispatchUpdatesTo(postListAdapter)
        } else{
          postListAdapter.updatePostList(results)
            postListAdapter.notifyDataSetChanged()
        }
    }

    fun onClick_getServersActivity(view:View){
        val intent = Intent(view.context, ServersActivity::class.java)
        view.context.startActivity(intent)
    }


    fun onClick_getAddYourServerActivity(view:View){
      //  val intent = Intent(view.context,AddYourServerActivity::class.java)
      //  view.context.startActivity(intent)
    }


    fun getHostingAdapter():HostingAdapter= hostAdapter

    fun loadListHosting(){
        postApi.getAllHosting()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({result -> onResultOfHostings(result)
                    loadingHostingCard.value = View.VISIBLE
                    progressHostingCard.value = View.GONE},
                        {oError()})
    }

    fun onResultOfHostings(list:List<ListHostings>){
        hostAdapter.setList(list.map{ info-> info.name }.subList(0,3))
    }


    fun loadListGames(){
        postApi.getAllGames()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({result -> onResultOfGames(result.result)
                    loadingGameCard.value = View.VISIBLE
                    progressGameCard.value = View.GONE},
                        {oError()})
    }

    fun onResultOfGames(games:List<InfoAboutGame>){
        gameAdapter.setList(games.map{ info-> info.name }.subList(1,4))
    }

}