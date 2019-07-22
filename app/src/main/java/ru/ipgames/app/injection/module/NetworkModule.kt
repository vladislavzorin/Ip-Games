package ru.ipgames.app.injection.module

import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.ipgames.app.adapters.MainServersAdapter
import ru.ipgames.app.adapters.OnlinePlayersAdapter
import ru.ipgames.app.adapters.ServerAdapter
import ru.ipgames.app.network.AppApi
import ru.ipgames.app.utils.BASE_URL

/**
 * Module which provides all required dependencies about network
 */
@Module
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
object NetworkModule {
    /**
     * Provides the Server service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the Server service implementation.
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun providePostApi(retrofit: Retrofit): AppApi {
        return retrofit.create(AppApi::class.java)
    }

    /**
     * Provides the Retrofit object.
     * @return the Retrofit object
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofitInterface(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }


    @Provides
    @Reusable
    @JvmStatic
    internal fun provideAdapter(): ServerAdapter {
        return ServerAdapter()
    }


    @Provides
    @Reusable
    @JvmStatic
    internal fun provideMainAdapter(): MainServersAdapter {
        return MainServersAdapter()
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideOnlinePlayersAdapter(): OnlinePlayersAdapter {
        return OnlinePlayersAdapter()
    }

}