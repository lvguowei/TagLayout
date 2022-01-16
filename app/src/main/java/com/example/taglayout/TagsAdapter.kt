package com.example.taglayout

import android.view.View
import android.view.ViewGroup

abstract class TagsAdapter {

    abstract fun getCount(): Int

    abstract fun getView(position: Int, parent: ViewGroup): View
}
