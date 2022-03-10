package com.maxkohl.speedreddit.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.maxkohl.speedreddit.R
import com.maxkohl.speedreddit.data.RedditPost
import com.maxkohl.speedreddit.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

const val FRAGMENT_TITLE = "Top 100"

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
        homeViewModel.getRedditPostsList()

        binding.lifecycleOwner = this
        binding.viewModel = homeViewModel
        binding.postsRecyclerview.adapter = HomeListAdapter(RedditPostListener { redditPost ->
            handleNav(redditPost)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = FRAGMENT_TITLE
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = FRAGMENT_TITLE

    }

    private fun handleNav(redditPost: RedditPost) {
        val bundle = Bundle().apply {
            putSerializable("redditPost", redditPost)
        }

        if (!redditPost.mediaType.isNullOrEmpty()) {
            when (redditPost.mediaType) {
                "text" -> findNavController().navigate(R.id.action_homeFragment_to_textFragment, bundle)
                "hosted:video" -> findNavController().navigate(R.id.action_homeFragment_to_videoFragment, bundle)
                "rich:video" -> findNavController().navigate(R.id.action_homeFragment_to_videoFragment, bundle)
                "image" -> findNavController().navigate(R.id.action_homeFragment_to_imageFragment, bundle)
                else -> {
                    val defaultBrowser = Intent.makeMainSelectorActivity(
                        Intent.ACTION_MAIN,
                        Intent.CATEGORY_APP_BROWSER
                    )
                    defaultBrowser.data = Uri.parse(redditPost.contentUrl)
                    startActivity(defaultBrowser)
                }
            }
            return
        }

        if (redditPost.isSelfPost) return findNavController().navigate(R.id.action_homeFragment_to_textFragment, bundle)
    }
}