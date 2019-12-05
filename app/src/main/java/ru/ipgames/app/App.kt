package ru.ipgames.app

import android.app.Application
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import ru.ipgames.app.injection.component.DaggerViewModelInjector
import ru.ipgames.app.injection.component.ViewModelInjector
import ru.ipgames.app.injection.module.NetworkModule

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        initDagger()

    }

    companion object {
        private lateinit var injector: ViewModelInjector
        fun initDagger(){
            injector = DaggerViewModelInjector
                .builder()
                .networkModule(NetworkModule)
                .build()
        }

        fun component():ViewModelInjector = injector
    }


}