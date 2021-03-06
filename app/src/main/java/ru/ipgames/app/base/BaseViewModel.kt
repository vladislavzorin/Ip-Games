package ru.ipgames.app.base

import androidx.lifecycle.ViewModel
import ru.ipgames.app.App
import ru.ipgames.app.injection.component.ViewModelInjector
import ru.ipgames.app.viewModels.*

abstract class BaseViewModel:ViewModel(){
    private val injector:ViewModelInjector = App.component()

    init {
        inject()
    }

    private fun inject() {
        when (this) {
            is MainFragmentViewModel -> injector.inject(this)
            is ServersViewModel -> injector.inject(this)
            is ServerInfoViewModel -> injector.inject(this)
            is HostingsFragmentViewModel -> injector.inject(this)
            is GamesFragmentViewModel -> injector.inject(this)
            is AddYourServerViewModel ->injector.inject(this)
            is FavoriteServersFragmentViewModel ->injector.inject(this)
            //is ServerItemViewModel -> injector.inject(this)
        }
    }
}