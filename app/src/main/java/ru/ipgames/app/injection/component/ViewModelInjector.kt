package ru.ipgames.app.injection.component

import dagger.Component
import ru.ipgames.app.activities.SearchActivity
import ru.ipgames.app.viewModels.GamesFragmentViewModel
import ru.ipgames.app.injection.module.NetworkModule
import ru.ipgames.app.viewModels.*
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector{

    fun inject(serverListViewModel: MainFragmentViewModel)

    fun inject(serversViewModel: ServersViewModel)

    fun inject(serverInfoViewModel: ServerInfoViewModel)

    fun inject(addYourServerViewModel: AddYourServerViewModel)

    fun inject(searchActivity: SearchActivity)

    fun inject(hostingsFragmentViewModel: HostingsFragmentViewModel)

    fun inject(gamesFragmentViewModel: GamesFragmentViewModel)

    fun inject(favoriteServersFragmentViewModel: FavoriteServersFragmentViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector
        fun networkModule(networkModule: NetworkModule): Builder
    }
}