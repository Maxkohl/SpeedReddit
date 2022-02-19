package com.maxkohl.speedreddit

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maxkohl.speedreddit.data.RedditPost
import com.maxkohl.speedreddit.home.HomeListAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<RedditPost>?) {
    val adapter = recyclerView.adapter as HomeListAdapter
    adapter.submitList(data)
}