package com.maxkohl.speedreddit.rapidmode

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.maxkohl.speedreddit.data.RedditPost

class RapidModeAdapter(rapidModeFragment: RapidModeFragment, val redditPosts: List<RedditPost>) :
    FragmentStateAdapter(rapidModeFragment) {

    override fun getItemCount(): Int {
        return redditPosts.size
    }

    override fun createFragment(position: Int): Fragment {
        val redditPost = redditPosts[position]

        if (redditPost.isSelfPost) return TextFragment.newInstance(redditPost)
        return when (redditPost.mediaType) {
            "image" -> ImageFragment.newInstance(redditPost)
            "rich:video" -> VideoFragment.newInstance(redditPost)
            "hosted:video" -> VideoFragment.newInstance(redditPost)
            else -> LinkFragment.newInstance(redditPost)
        }
    }
}