package ru.ipgames.app.viewModels

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.lifecycle.MutableLiveData
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.ipgames.app.adapters.HostingAndGameAdapter
import ru.ipgames.app.adapters.MainServersAdapter
import ru.ipgames.app.R
import ru.ipgames.app.base.BaseViewModel
import ru.ipgames.app.fragments.AddYourServerFragment
import ru.ipgames.app.fragments.GamesFragment
import ru.ipgames.app.fragments.HostingsFragment
import ru.ipgames.app.fragments.ServersListFragment
import ru.ipgames.app.model.InfoAboutGame
import ru.ipgames.app.model.ListHostings
import ru.ipgames.app.model.Servers
import ru.ipgames.app.model.ServersDao
import ru.ipgames.app.network.AppApi
import ru.ipgames.app.utils.BUY_URL
import ru.ipgames.app.utils.MainServersDiffUtilCallBackCallback
import ru.ipgames.app.utils.TWITTER_URL
import ru.ipgames.app.utils.VK_GROUP_URL
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
    val errorClickListener = View.OnClickListener { }
    var isFirstLoad:Boolean = true
    var isDBObservable:Boolean = false

    val loadingGameCard = MutableLiveData<Int>()
    val progressGameCard = MutableLiveData<Int>()
    val loadingHostingCard = MutableLiveData<Int>()
    val progressHostingCard = MutableLiveData<Int>()
    val loadingMainCard = MutableLiveData<Int>()
    val progressMainCard = MutableLiveData<Int>()

    private  var compositeDisposable: CompositeDisposable = CompositeDisposable()
    lateinit var activityFragment: FragmentActivity

    init{
        loadListGames()
        loadListHosting()
    }

    fun unSubscribeAll(){
        compositeDisposable.dispose()
        compositeDisposable = CompositeDisposable()
    }

    private fun onRetrievePostListFinish(){
        loadingVisibility.value = View.GONE
    }

    fun loadServers2(page:Int){
        compositeDisposable.add(
            appApi.getServersMain(page)
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
        )

    }

    fun  oError(){
        Log.d("mLog","-ERROR-")
    }

    fun onResult(postList:List<Servers>)
    {
        compositeDisposable.add(
        Observable.fromCallable {postDao.all}
                .concatMap{
                    dbPostList ->
                    if(dbPostList.isEmpty()) {
                        postDao.insertAll(*postList.toTypedArray())
                        ObservDB()
                    } else { if(isFirstLoad && !isDBObservable) {
                                                                    postDao.deleteAll()
                                                                    isFirstLoad=false
                                                                    ObservDB()
                                                                }

                            val insertList: ArrayList<Servers> = ArrayList()
                            val updateList: ArrayList<Servers> = ArrayList()
                            for (i in 0..9) {
                                if (postDao.countID(postList.toTypedArray()[i].address) == 0) {
                                    insertList.add(postList.toTypedArray()[i])
                                } else {
                                    updateList.add(postList.toTypedArray()[i])
                                }
                            }

                            if (insertList.size > 0) {
                                postDao.insertAll(*insertList.toTypedArray())
                            }

                            if (updateList.size > 0) {
                                postDao.updateAll(*updateList.toTypedArray())
                            }

                    }
                    Observable.just(postDao.all)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({},{})
        )
    }

    fun ObservDB(){
        if (!isDBObservable) {
            isDBObservable=true
            compositeDisposable.add(
                postDao.all()
                    .take(10)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ res -> updateRV(res) }, {
                        it.printStackTrace()
                    })
            )
        }
    }

    fun updateRV(results:List<Servers>)
    {
        if (postListAdapter.itemCount !=0) {
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
        activityFragment.supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentLayout, ServersListFragment())
            .commit()
    }

    fun onClick_getAddYourServerActivity(view:View){
        activityFragment.supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentLayout, AddYourServerFragment())
            .commit()
    }

    @SuppressLint("CheckResult")
    fun loadListHosting(){
        appApi.getAllHosting()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen  {ob -> ob.take(3).delay(10,TimeUnit.SECONDS)}
                .subscribe({result -> onResultOfHostings(result)
                    loadingHostingCard.value = View.VISIBLE
                    progressHostingCard.value = View.GONE},
                        {oError()})
    }

    fun onResultOfHostings(list:List<ListHostings>){
        hostAdapter.setList(list.map{ info-> info.name }.subList(0,3))
    }


    fun loadListGames(){
        compositeDisposable.add(
            appApi.getAllGames()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen  {ob -> ob.take(3).delay(10,TimeUnit.SECONDS)}
                .subscribe({result -> onResultOfGames(result.result)
                    loadingGameCard.value = View.VISIBLE
                    progressGameCard.value = View.GONE},
                        {oError()})
        )
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

    fun  onClick_socialIcon_Twitter(view:View){
        startActivity(this.activityFragment,Intent(Intent.ACTION_VIEW, Uri.parse(TWITTER_URL)),null)
    }

    fun  onClick_socialIcon_Vk(view:View){
        startActivity(view.context,Intent(Intent.ACTION_VIEW, Uri.parse(VK_GROUP_URL)),null)
    }

    fun onClick_buyThis(view:View){
        startActivity(view.context,Intent(Intent.ACTION_VIEW, Uri.parse(BUY_URL)),null)
    }

    fun onClick_about(view:View){
        val dialog =  Dialog(view.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_info)
        dialog.setCancelable(true)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        dialog.show()
        dialog.window.attributes = lp
    }

}