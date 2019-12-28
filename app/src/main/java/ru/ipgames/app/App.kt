package ru.ipgames.app

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import ru.ipgames.app.injection.component.DaggerViewModelInjector
import ru.ipgames.app.injection.component.ViewModelInjector
import ru.ipgames.app.injection.module.NetworkModule
import ru.ipgames.app.model.database.AppDatabaseFavorite

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        initDagger()
        val db = Room.databaseBuilder(this.applicationContext, AppDatabaseFavorite::class.java, "favoriteServers").build()
        initDB(db)
        MobileAds.initialize(this.applicationContext, "ca-app-pub-6501617133685717~9414297692")
    }

    companion object {
        private lateinit var injector: ViewModelInjector
        private lateinit var db: AppDatabaseFavorite

        fun initDagger(){
            injector = DaggerViewModelInjector
                .builder()
                .networkModule(NetworkModule)
                .build()
        }

        fun initDB(database:AppDatabaseFavorite){
            db = database
        }

        fun component():ViewModelInjector = injector

        fun getDB():AppDatabaseFavorite = db

        fun getAdRequest():AdRequest {
            return AdRequest.Builder().build()
        }
    }


}