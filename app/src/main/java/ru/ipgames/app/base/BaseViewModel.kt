package ru.ipgames.app.base

import android.arch.lifecycle.ViewModel
import ru.ipgames.app.injection.component.DaggerViewModelInjector
import ru.ipgames.app.injection.component.ViewModelInjector
import ru.ipgames.app.injection.module.NetworkModule
import ru.ipgames.app.viewModels.*


abstract class BaseViewModel:ViewModel(){
    private val injector: ViewModelInjector = DaggerViewModelInjector
            .builder()
            .networkModule(NetworkModule)

            .build()

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
            //is PostViewModel -> injector.inject(this)
        }
    }
}