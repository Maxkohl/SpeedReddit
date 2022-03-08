package com.maxkohl.speedreddit.rapidmode

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.maxkohl.speedreddit.data.RedditPost

class RapidModeAdapter(rapidModeFragment: RapidModeFragment, val redditPosts: List<RedditPost>) :
    FragmentStateAdapter(rapidModeFragment) {

    override fun getItemCount(): Int {
        return redditPosts.size
    }


    override fun createFragment(position: Int): Fragment {
        val redditPost = redditPosts[position]
        return when (redditPost.mediaType) {
            "image" -> RapidImageFragment.newInstance(redditPost)
            "hosted:video" -> RapidVideoFragment.newInstance(redditPost)
            else -> RapidIntroFragment()
        }
    }
}