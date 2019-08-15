package ru.ipgames.app.viewModels

import android.arch.lifecycle.MutableLiveData
import android.view.View
import ru.ipgames.app.R

import ru.ipgames.app.base.BaseViewModel

class AddYourServerViewModel: BaseViewModel() {

    var isListVisible:MutableLiveData<Int> = MutableLiveData()
    var isIpVisible:MutableLiveData<Int> = MutableLiveData()
    var isConfirmVisible:MutableLiveData<Int> = MutableLiveData()

    var numberOfClick:MutableLiveData<Int> = MutableLiveData()
    var idOfTitleClick:MutableLiveData<Int> = MutableLiveData()

    var success_step = 0
    var current_step = 0

    init{
        isListVisible.value = View.VISIBLE
        isIpVisible.value = View.GONE
        isConfirmVisible.value = View.GONE
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

            }
        }

    }

}