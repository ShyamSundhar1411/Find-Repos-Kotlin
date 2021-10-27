package com.shyam.findrepos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchEditText = findViewById<EditText>(R.id.keywordBox)
        var searchbutton= findViewById<Button>(R.id.search)
        searchbutton.setOnClickListener {
            var searchresultintent = Intent(getApplicationContext(),SearchResultActivity::class.java)
            searchresultintent.putExtra("keyword",searchEditText.text.toString())
            startActivity(searchresultintent)
        }
    }
}