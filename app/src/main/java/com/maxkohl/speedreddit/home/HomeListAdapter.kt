package com.maxkohl.speedreddit.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.maxkohl.speedreddit.data.RedditPost
import com.maxkohl.speedreddit.databinding.ListViewItemBinding

class HomeListAdapter : ListAdapter<RedditPost, HomeListAdapter.HomeViewHolder>(DiffCallback) {

    class HomeViewHolder(
        var binding: ListViewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(redditPost: RedditPost) {
            binding.redditPost = redditPost
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HomeViewHolder(
            ListViewItemBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val redditPost = getItem(position)
        holder.bind(redditPost)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<RedditPost>() {

        override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
            //TODO Change these values being checked to something more meaningful
            return oldItem.title == newItem.title && oldItem.imgSrc == newItem.imgSrc
        }
    }
}
