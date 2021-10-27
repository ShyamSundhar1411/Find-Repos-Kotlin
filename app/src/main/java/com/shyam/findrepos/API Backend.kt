package com.shyam.findrepos

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubService{
    @GET("search/repositories?")
    fun searchRepos(@Query("q") q:String) : Call<GithubSearchResult>
}
class GithubSearchResult(val items : List<Repos>)
class Repos(val full_name : String, val owner: Owner,val html_url: String)
class Owner(val avatar_url : String)
class RepoRetriever{
    val service : GitHubService
    init{
        val retrofit = Retrofit.Builder().baseUrl("https://api.github.com/").addConverterFactory(
            GsonConverterFactory.create()).build()
        service = retrofit.create(GitHubService::class.java)
    }
    fun getRepos(callBack: Callback<GithubSearchResult>,searchTerm:String){
        var searchT = searchTerm
        if (searchT == ""){
            searchT = "Django"
        }
        val call = service.searchRepos(searchT)
        call.enqueue(callBack)
    }
}