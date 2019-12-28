package ru.ipgames.app.injection.module

import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.ipgames.app.App
import ru.ipgames.app.adapters.MainServersAdapter
import ru.ipgames.app.adapters.OnlinePlayersAdapter
import ru.ipgames.app.adapters.ServerAdapter
import ru.ipgames.app.model.FavoriteServersDao
import ru.ipgames.app.model.database.AppDatabase
import ru.ipgames.app.model.database.AppDatabaseFavorite
import ru.ipgames.app.network.AppApi
import ru.ipgames.app.utils.BASE_URL
import javax.inject.Singleton


@Module
@Suppress("unused")
object NetworkModule {

    @Provides
    @Singleton
    internal fun providePostApi(retrofit: Retrofit): AppApi {
        return retrofit.create(AppApi::class.java)
    }

    @Provides
    @Singleton
    internal fun provideRetrofitInterface(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }


    @Provides
    @Singleton
    internal fun provideAdapter(): ServerAdapter {
        return ServerAdapter()
    }


    @Provides
    @Singleton
    internal fun provideMainAdapter(): MainServersAdapter {
        return MainServersAdapter()
    }

    @Provides
    @Singleton
    internal fun provideOnlinePlayersAdapter(): OnlinePlayersAdapter {
        return OnlinePlayersAdapter()
    }



    @Provides
    @Singleton
    internal fun provideDataBase(): FavoriteServersDao {
        return App.getDB().favoriteServersDao()
    }
}