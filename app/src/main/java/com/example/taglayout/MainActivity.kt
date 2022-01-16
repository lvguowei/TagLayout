package com.example.taglayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    val tags = listOf(
        "books",
        "book recommendations",
        "recommendations",
        "recommendation",
        "books to read",
        "reading recommendations",
        "best books",
        "favourite books",
        "favorite books",
        "book recommendation",
        "books you need to read",
        "booktube recommendations"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tagLayout: TagLayout = findViewById(R.id.tagLayout)
        tagLayout.adapter = object : TagsAdapter() {
            override fun getCount(): Int {
                return tags.size
            }

            override fun getView(position: Int, parent: ViewGroup): View {
                return LayoutInflater.from(this@MainActivity)
                    .inflate(R.layout.tag_item_layout, parent, false).apply {
                        findViewById<TextView>(R.id.tag).text = tags[position]
                    }
            }
        }
    }
}
