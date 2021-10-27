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
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        val searchTerm = intent.getStringExtra("keyword")
        val retriever = RepoRetriever()
        if (searchTerm != null) {
            val callBack = object:Callback<GithubSearchResult>{
                override fun onResponse(
                    call: Call<GithubSearchResult>,
                    response: Response<GithubSearchResult>
                ) {
                    val searchResult = response?.body()
                    if (searchResult != null){
                        listRepos(searchResult!!.items)
                    }
                }

                override fun onFailure(call: Call<GithubSearchResult>, t: Throwable) {
                    println("Not working")
                }

            }
            retriever.getRepos(callBack,searchTerm)
        }
        else{
            val username = intent.getStringExtra("userName")

            val callBack = object : Callback<List<Repos>>{
                override fun onResponse(call: Call<List<Repos>>, response: Response<List<Repos>>) {
                    if (response.code() == 404){
                        Toast.makeText(this@SearchResultActivity, "User Does Not Exist !!", Toast.LENGTH_LONG).show()
                    }
                    else {
                        val repos = response?.body()
                        if (repos != null) {
                            for (repo in repos!!) {
                                println(repo.html_url)
                            }
                            listRepos(repos)
                        }
                    }
                }

                override fun onFailure(call: Call<List<Repos>>, t: Throwable) {
                    println("Not Working")
                }


            }
            if (username!=null) {
                retriever.userRepos(callBack, username)
            }
            else{
                retriever.userRepos(callBack,"")
            }

        }


    }
    fun listRepos(repos: List<Repos>){
        val listView = findViewById<ListView>(R.id.RepoListView)
        listView.setOnItemClickListener { adapterView, view, i, l ->
            val selected = repos!![i]
            val url = Uri.parse(selected.html_url)
            val finishIntent = Intent(Intent.ACTION_VIEW,url)
            startActivity(finishIntent)
        }
        val adapter = RepoAdapter(getApplicationContext(),android.R.layout.simple_expandable_list_item_1,repos!!)
        listView.adapter = adapter
    }
    class RepoAdapter(context: Context, resource: Int, objects: List<Repos>) : ArrayAdapter<Repos>(context, resource, objects){
        override fun getCount(): Int {
            return super.getCount()
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val repoView = inflator.inflate(R.layout.repo_list_layout,parent,false)
            val textView = repoView.findViewById<TextView>(R.id.RepoTextView)
            val imageView = repoView.findViewById<ImageView>(R.id.RepoImageView)
            val repo = getItem(position)
            Picasso.with(context).load(Uri.parse(repo?.owner?.avatar_url)).into(imageView)
            textView.text = repo?.full_name
            return repoView
        }
    }
}
