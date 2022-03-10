package com.maxkohl.speedreddit.postmedia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.maxkohl.speedreddit.data.RedditPost
import com.maxkohl.speedreddit.databinding.FragmentImageBinding

class ImageFragment : Fragment() {

    lateinit var redditPost: RedditPost

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            redditPost = it.getSerializable("redditPost") as RedditPost
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val binding = FragmentImageBinding.inflate(inflater, container, false)
        binding.redditPost = redditPost

        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
    }

    companion object {
        @JvmStatic
        fun newInstance(redditPost: RedditPost) =
            ImageFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("redditPost", redditPost)
                }
            }
    }
}