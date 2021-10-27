package com.shyam.findrepos

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.LiveFolders.INTENT
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
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
                    val listView = findViewById<ListView>(R.id.RepoListView)
                    listView.setOnItemClickListener { adapterView, view, i, l ->
                        val selected = searchResult!!.items[i]
                        val url = selected.html_url
                        val finishIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(finishIntent)
                    }
                    val adapter = RepoAdapter(getApplicationContext(),android.R.layout.simple_expandable_list_item_1,searchResult.items)
                    listView.adapter = adapter
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
    class RepoAdapter(context: Context, resource: Int, objects: List<Repos>) : ArrayAdapter<Repos>(context, resource, objects){
        override fun getCount(): Int {
            return super.getCount()
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val repoView = inflator.inflate(android.R.layout.simple_list_item_1,parent,false) as TextView
            val repo = getItem(position)
            repoView.text = repo?.full_name
            return repoView
        }
    }
}
