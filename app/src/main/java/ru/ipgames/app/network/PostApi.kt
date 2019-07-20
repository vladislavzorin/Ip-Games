package ru.ipgames.app.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.ipgames.app.model.*
import ru.ipgames.app.utils.KEY


interface PostApi {

    @GET("/method/servers_list_Android.php?key=$KEY&limit=50")
    fun getPosts(@Query("page") page:Int): Observable<List<Post>>

    @GET("/method/servers_list_Android.php?key=$KEY&limit=10")
    fun getServersMain(@Query("page") page:Int): Observable<List<Servers>>

    @GET("/method/server.get?key=$KEY")
    fun getInfoAboutServer(@Query("address") ip:String): Observable<ResultInfo>

    @GET("/method/server.players?key=$KEY")
    fun getInfoAboutOnlinePlayersInServer(@Query("address") ip:String): Observable<OnlinePlayers>

    @GET("/method/server.stats?key=$KEY")
    fun getServerStats(@Query("address") ip:String): Observable<List<Stats>>

    @GET("/method/hosting.all?key=$KEY")
    fun getAllHosting():Observable<List<ListHostings>>

    @GET("/method/hosting.get?key=$KEY")
    fun getInfoAboutHosting(@Query("id")id:Int):Observable<InfoAboutHosting>

    @GET("/method/games.all?key=$KEY")
    fun getAllGames():Observable<Games>

}