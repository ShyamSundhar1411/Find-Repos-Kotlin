package com.shyam.findrepos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        val searchTerm = intent.getStringExtra("keyword")
        val callBack = object:Callback<GithubSearchResult>{
            override fun onResponse(
                call: Call<GithubSearchResult>,
                response: Response<GithubSearchResult>
            ) {
               val searchResult = response?.body()
                if (searchResult != null){
                    for (i in searchResult!!.items){
                        println(i.full_name)
                    }
                }
            }

            override fun onFailure(call: Call<GithubSearchResult>, t: Throwable) {
                println("Not working")
            }

        }
        val retriever = RepoRetriever()
        if (searchTerm != null) {
            retriever.getRepos(callBack,searchTerm)
        }
    }
}