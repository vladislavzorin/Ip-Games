package ru.ipgames.app.viewModels

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.util.Log
import android.view.View
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.ipgames.app.activities.ServersActivity
import ru.ipgames.app.adapters.HostingAndGameAdapter
import ru.ipgames.app.adapters.MainServersAdapter
import ru.ipgames.app.R
import ru.ipgames.app.base.BaseViewModel
import ru.ipgames.app.fragments.GamesFragment
import ru.ipgames.app.fragments.HostingsFragment
import ru.ipgames.app.fragments.MainFragment
import ru.ipgames.app.model.InfoAboutGame
import ru.ipgames.app.model.ListHostings
import ru.ipgames.app.model.Servers
import ru.ipgames.app.model.ServersDao
import ru.ipgames.app.network.AppApi
import ru.ipgames.app.utils.MainServersDiffUtilCallBackCallback
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainFragmentViewModel(private val postDao: ServersDao): BaseViewModel(){
    @Inject
    lateinit var appApi: AppApi

    @Inject
    lateinit var postListAdapter: MainServersAdapter

    val hostAdapter:HostingAndGameAdapter = HostingAndGameAdapter()
    val gameAdapter:HostingAndGameAdapter = HostingAndGameAdapter()

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
    public  lateinit var activityFragment:FragmentActivity

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
        subscription = appApi.getServersMain(page)
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

                            val insertList: ArrayList<Servers> = ArrayList()
                            val updateList: ArrayList<Servers> = ArrayList()
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


    fun getHostingAdapter():HostingAndGameAdapter= hostAdapter

    fun loadListHosting(){
        appApi.getAllHosting()
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
        appApi.getAllGames()
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

    fun onClick_getGameFragment(){
        activityFragment.supportFragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragmentLayout, GamesFragment())
                        .commit()
    }

    fun onClick_getHostingsFragment(){
        activityFragment.supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentLayout, HostingsFragment())
            .commit()
    }

}