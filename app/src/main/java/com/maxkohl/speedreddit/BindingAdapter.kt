package com.maxkohl.speedreddit

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.decode.ImageDecoderDecoder
import coil.load
import com.maxkohl.speedreddit.data.RedditPost
import com.maxkohl.speedreddit.home.HomeListAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<RedditPost>?) {
    val adapter = recyclerView.adapter as HomeListAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri) {
            placeholder(R.drawable.reddit_snoo)
            error(R.drawable.reddit_snoo)
            decoder(ImageDecoderDecoder(imgView.context))
        }
    }
}