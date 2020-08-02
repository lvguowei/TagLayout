package com.example.taglayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  private val items =
    listOf(
      "Structure and Interpretation of Computer Programs",
      "On Lisp",
      "Little Lisper",
      "Unix Power Tools",
      "Emacs Manual",
      "PICO-8 Game Programming"
    )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    tagLayout.adapter = object : TagsAdapter() {
      override fun getCount(): Int {
        return items.size
      }

      override fun getView(position: Int, parent: ViewGroup): View {
        return (LayoutInflater.from(this@MainActivity)
          .inflate(R.layout.item_tag, parent, false) as TextView).apply {
          text = items[position]
        }
      }
    }
  }
}