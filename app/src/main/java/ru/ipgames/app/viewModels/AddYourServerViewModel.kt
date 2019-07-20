package ru.ipgames.app.viewModels

import android.arch.lifecycle.MutableLiveData
import android.view.View
import ru.ipgames.app.R

import ru.ipgames.app.base.BaseViewModel

class AddYourServerViewModel: BaseViewModel() {

    fun getVisibilityView(view: View): MutableLiveData<Int>{
        val flag:MutableLiveData<Int> = MutableLiveData()
        when (view.id){
            R.id.lyt_title -> {
                flag.value = View.VISIBLE
                return flag
            }

            R.id.lyt_description -> {
                flag.value = View.GONE
                return flag
            }

            R.id.lyt_confirmation -> {
                flag.value = View.GONE
                return flag
            }

        }
        return flag
    }


}