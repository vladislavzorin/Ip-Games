package ru.ipgames.app.viewModels

import android.arch.lifecycle.MutableLiveData
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.ipgames.app.R

import ru.ipgames.app.base.BaseViewModel
import ru.ipgames.app.network.AppApi
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AddYourServerViewModel: BaseViewModel() {

    var isListVisible:MutableLiveData<Int> = MutableLiveData()
    var isIpVisible:MutableLiveData<Int> = MutableLiveData()
    var isConfirmVisible:MutableLiveData<Int> = MutableLiveData()

    var isButtonAddVisible:MutableLiveData<Int> = MutableLiveData()
    var isTextAddVisible:MutableLiveData<Int> = MutableLiveData()
    var getResultTextAdd:MutableLiveData<String> = MutableLiveData()
    var getResultBooleanAdd:MutableLiveData<Boolean> = MutableLiveData()
    var isProgressBarAddVisible:MutableLiveData<Int> = MutableLiveData()

    var numberOfClick:MutableLiveData<Int> = MutableLiveData()
    var idOfTitleClick:MutableLiveData<Int> = MutableLiveData()

    var ip:String = ""
    var gameId:String = ""

    @Inject
    lateinit var appApi: AppApi

    init{
        // настройка видимости разделов
        isListVisible.value = View.VISIBLE
        isIpVisible.value = View.GONE
        isConfirmVisible.value = View.GONE

        // настройка видимости элементов последнего раздела
        isButtonAddVisible.value = View.VISIBLE
        isTextAddVisible.value = View.GONE
        isProgressBarAddVisible.value = View.GONE
    }

    fun onTitleClick(view: View){
        idOfTitleClick.value = view.id
    }

    fun onClick(view:View){
        when (view.id){
            R.id.bt_continue_title ->{
                numberOfClick.value = 0
            }

            R.id.bt_continue_description ->{
                numberOfClick.value = 1
            }

            R.id.bt_add_event ->{
                addServer(ip,gameId)
            }
        }
    }

    //запрос на сервер
    fun addServer(serverIp:String,serverGameId:String){

        isButtonAddVisible.value = View.GONE
        isProgressBarAddVisible.value = View.VISIBLE

        appApi.addServer(serverIp,serverGameId,0)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate {
                isProgressBarAddVisible.value = View.GONE
                isTextAddVisible.value = View.VISIBLE
            }
            .retryWhen{ob -> ob.take(2).delay(5, TimeUnit.SECONDS)}
            .subscribe({response -> getTextResult(response.success,response.comment?:" ")}
                ,{it.printStackTrace()})
    }

    fun getTextResult(result:Boolean,text:String){
        getResultTextAdd.value = if (result) "Сервер добавлен!" else "Сервер не был добавлен! Ошибка: $text"
        getResultBooleanAdd.value = result
    }

}